package kpn.core.tools.analysis

import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy

import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RefChanges
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.common.Ref
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.route.RouteDiff
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.analysis.Network
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.overpass.QueryNode
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.engine.changes.node.NodeChangeAnalyzer
import kpn.server.analyzer.engine.changes.route.RouteChangeAnalyzer
import kpn.server.analyzer.load.data.LoadedNode
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

import scala.io.Source
import scala.xml.XML

/*
  Loads the initial state of the analysis and changes database with the oldest information that is
  available in the overpass database (2016-08-11 10:04:03 --> 1 second before the first changeset
  after the redaction of the OSM database as a result of the license change). For all nodes in the
  networks and all orphan nodes an initial NodeChange document is created. This initial NodeChange
  document can be used to display the oldest known state of the node in the node history.
  Likewise for routes, initial RouteChange objects are created.
 */
object AnalyzerStartTool {

  private val log = Log(classOf[AnalyzerStartTool])

  def main(args: Array[String]): Unit = {

    val exit = AnalyzerStartToolOptions.parse(args) match {
      case Some(options) =>

        log.info("Start")
        val executor = buildExecutor()
        try {
          val configuration = new AnalyzerStartToolConfiguration(executor, options)
          new AnalyzerStartTool(configuration).analyze()
        }
        finally {
          executor.shutdown()
          log.info(s"Done")
          ()
        }

        0

      case None =>
        // arguments are bad, error message will have been displayed
        -1
    }

    System.exit(exit)
  }

  private def buildExecutor(): ThreadPoolTaskExecutor = {
    val executor = new ThreadPoolTaskExecutor
    executor.setCorePoolSize(6)
    executor.setMaxPoolSize(6)
    executor.setRejectedExecutionHandler(new CallerRunsPolicy)
    executor.setThreadNamePrefix("analyzer-start-")
    executor.initialize()
    executor
  }
}

class AnalyzerStartTool(config: AnalyzerStartToolConfiguration) {

  private val log = AnalyzerStartTool.log

  private val tempBlacklistedRouteIds = { // TODO remove this temporary code
    val source = Source.fromFile("/kpn/conf/temp-blacklist.txt")
    val res = source.getLines().map(_.toLong).toSet
    source.close()
    res
  }

  def analyze(): Unit = {

    loadNetworks()
    loadOrphanRoutes()
    loadOrphanNodes()

    buildNetworkChanges()
    buildOrphanRouteChanges()
    buildOrphanNodeChanges()
  }

  private def loadNetworks(): Unit = {
    Log.context("networks") {
      val networkIds = config.analysisContext.snapshotKnownElements.networkIds.toSeq.sorted
      log.info(s"Trying to load a maximum of ${networkIds.size} networks (using networkIds in snapshot)")
      config.networkInitialLoader.load(config.timestamp, networkIds)
      log.info(s"${config.analysisContext.data.networks.watched.size} networks loaded")
    }
  }

  private def loadOrphanRoutes(): Unit = {
    config.analysisDatabaseIndexer.index(true)
    Log.context("orphan-routes") {
      val allRouteIds = config.routeRepository.allRouteIds()
      val orphanRouteIds = config.analysisContext.snapshotKnownElements.routeIds -- allRouteIds.toSet -- tempBlacklistedRouteIds
      log.info(s"Trying to load a maximum of ${orphanRouteIds.size} orphan routes")
      orphanRouteIds.toSeq.sorted.zipWithIndex.foreach { case (routeId, index) =>
        Log.context(s"${index + 1}/${orphanRouteIds.size}") {
          config.orphanRoutesLoaderWorker.process(config.timestamp, routeId)
        }
      }
      log.info(s"${config.analysisContext.data.orphanRoutes.watched.size} orphan routes loaded")
    }
  }

  private def loadOrphanNodes(): Unit = {
    config.analysisDatabaseIndexer.index(true)
    val allNodeIds = config.nodeRepository.allNodeIds()
    val orphanNodeIds = config.analysisContext.snapshotKnownElements.nodeIds -- allNodeIds
    Log.context("orphan-nodes") {
      log.info(s"Trying to load a maximum of ${orphanNodeIds.size} orphan nodes")
      orphanNodeIds.toSeq.sorted.zipWithIndex.foreach { case (nodeId, index) =>
        Log.context(s"${index + 1}/${orphanNodeIds.size}") {
          val xmlString = config.nonCachingExecutor.executeQuery(Some(config.timestamp), QueryNode(nodeId))
          val xml = XML.loadString(xmlString)
          val rawData = new Parser().parse(xml.head)
          val data = new DataBuilder(rawData).data
          data.nodes.get(nodeId) match {
            case Some(node) =>
              val countries = config.countryAnalyzer.countries(node)
              val loadedNode = LoadedNode.from(countries.headOption, node.raw)
              val nodeInfo = config.nodeInfoBuilder.fromLoadedNode(loadedNode, orphan = true)
              config.analysisContext.data.orphanNodes.watched.add(nodeId)
              config.analysisRepository.saveNode(nodeInfo)

            case _ => // node not found
          }
        }
      }
      log.info(s"${config.analysisContext.data.orphanNodes.watched.size} orphan nodes loaded")
    }
  }

  private def buildNetworkChanges(): Unit = {
    config.analysisDatabaseIndexer.index(true)
    val networkIds = config.networkRepository.allNetworkIds()
    networkIds.zipWithIndex.foreach { case (networkId, index) =>
      Log.context(s"network=$networkId ${index + 1}/${networkIds.size}") {
        log.unitElapsed {
          config.networkLoader.load(Some(config.timestamp), networkId) match {
            case Some(loadedNetwork) =>
              val networkRelationAnalysis = config.networkRelationAnalyzer.analyze(loadedNetwork.relation)
              log.info(s"""Analyze "${loadedNetwork.name}"""")
              val network = config.networkAnalyzer.analyze(networkRelationAnalysis, loadedNetwork)
              buildNetworkChange(network)
              buildNetworkRouteChanges(network)
              buildNetworkNodeChanges(network)
              loadedNetwork.name

            case None =>
              s"Failed to load network $networkId"
          }
        }
      }
    }
  }

  private def buildNetworkChange(network: Network): Unit = {

    val nodeRefs = network.nodes.map(node => Ref(node.id, node.networkNode.name))
    val routeRefs = network.routes.map(route => Ref(route.id, route.routeAnalysis.name))
    config.changeSetRepository.saveNetworkChange(
      NetworkChange(
        key = config.changeSetContext.buildChangeKey(network.id),
        changeType = ChangeType.InitialValue,
        network.country,
        network.networkType,
        network.id,
        network.name,
        orphanRoutes = RefChanges(),
        orphanNodes = RefChanges(),
        networkDataUpdate = None,
        networkNodes = RefDiffs(added = nodeRefs),
        routes = RefDiffs(added = routeRefs),
        nodes = IdDiffs(),
        ways = IdDiffs(),
        relations = IdDiffs(),
        happy = true,
        investigate = network.facts.nonEmpty
      )
    )
  }

  private def buildNetworkRouteChanges(network: Network): Unit = {
    network.routes.foreach { networkMemberRoute =>

      val factDiffs = if (networkMemberRoute.routeAnalysis.route.facts.nonEmpty) {
        Some(
          FactDiffs(
            remaining = networkMemberRoute.routeAnalysis.route.facts.toSet
          )
        )
      }
      else {
        None
      }

      val locations = networkMemberRoute.routeAnalysis.route.analysis.locationAnalysis.locationNames

      config.changeSetRepository.saveRouteChange(
        RouteChangeAnalyzer.analyzed(
          RouteChange(
            key = config.changeSetContext.buildChangeKey(networkMemberRoute.id),
            changeType = ChangeType.InitialValue,
            name = networkMemberRoute.routeAnalysis.route.summary.name,
            locationAnalysis = networkMemberRoute.routeAnalysis.route.analysis.locationAnalysis,
            addedToNetwork = Seq.empty,
            removedFromNetwork = Seq.empty,
            before = None,
            after = Some(networkMemberRoute.routeAnalysis.toRouteData),
            removedWays = Seq.empty,
            addedWays = Seq.empty,
            updatedWays = Seq.empty,
            diffs = RouteDiff(
              factDiffs = factDiffs
            ),
            facts = networkMemberRoute.routeAnalysis.route.facts
          )
        )
      )
    }
  }

  private def buildNetworkNodeChanges(network: Network): Unit = {
    network.nodes.foreach { node =>
      val networkTypes: Seq[NetworkType] = NodeAnalyzer.networkTypes(node.networkNode.tags)
      val subsets: Seq[Subset] = node.networkNode.country.toSeq.flatMap {
        c => networkTypes.flatMap(n => Subset.of(c, n))
      }

      val nodeChange = NodeChangeAnalyzer.analyzed(
        NodeChange(
          key = config.changeSetContext.buildChangeKey(node.id),
          changeType = ChangeType.InitialValue,
          subsets = subsets,
          location = node.networkNode.location,
          name = node.networkNode.name,
          before = None,
          after = Some(node.networkNode.node.raw),
          connectionChanges = Seq.empty,
          roleConnectionChanges = Seq.empty,
          definedInNetworkChanges = Seq.empty,
          tagDiffs = None,
          nodeMoved = None,
          addedToRoute = Seq.empty,
          removedFromRoute = Seq.empty,
          addedToNetwork = Seq.empty,
          removedFromNetwork = Seq.empty,
          factDiffs = FactDiffs(remaining = node.facts.toSet),
          facts = node.facts,
        )
      )
      config.changeSetRepository.saveNodeChange(NodeChangeAnalyzer.analyzed(nodeChange))
    }
  }

  private def buildOrphanRouteChanges(): Unit = {

    config.analysisDatabaseIndexer.index(true)

    val orphanRouteIds = Subset.all.flatMap { subset =>
      config.orphanRepository.orphanRoutes(subset).map(_.id)
    }

    orphanRouteIds.zipWithIndex.foreach {
      case (routeId, index) =>
        Log.context(s"route $routeId ${index + 1}/${orphanRouteIds.size}") {

          config.routeLoader.loadRoute(config.timestamp, routeId) match {
            case None => log.warn("Could not load route")
            case Some(loadedRoute) =>
              val analysis = config.routeAnalyzer.analyze(loadedRoute, orphan = true)
              val facts = analysis.route.facts :+ Fact.OrphanRoute

              config.changeSetRepository.saveRouteChange(
                RouteChangeAnalyzer.analyzed(
                  RouteChange(
                    key = config.changeSetContext.buildChangeKey(analysis.route.id),
                    changeType = ChangeType.InitialValue,
                    name = analysis.route.summary.name,
                    locationAnalysis = analysis.route.analysis.locationAnalysis,
                    addedToNetwork = Seq.empty,
                    removedFromNetwork = Seq.empty,
                    before = None,
                    after = Some(analysis.toRouteData),
                    removedWays = Seq.empty,
                    addedWays = Seq.empty,
                    updatedWays = Seq.empty,
                    diffs = RouteDiff(
                      factDiffs = Some(
                        FactDiffs(
                          remaining = facts.toSet
                        )
                      )
                    ),
                    facts = facts
                  )
                )
              )

              val routeNodes = analysis.startNodes ++
                analysis.endNodes ++
                analysis.startTentacleNodes ++
                analysis.endTentacleNodes

              val routeNodeIds = routeNodes.map(_.id)

              val loadedNodes: Seq[LoadedNode] = config.nodeLoader.loadNodes(config.timestamp, routeNodeIds)
              buildNodeChanges(loadedNodes)

          }
        }
    }
  }

  private def buildOrphanNodeChanges(): Unit = {

    config.analysisDatabaseIndexer.index(true)

    val orphanNodeIds = Subset.all.flatMap { subset =>
      config.orphanRepository.orphanNodes(subset).map(_.id)
    }

    val loadedNodes: Seq[LoadedNode] = config.nodeLoader.loadNodes(config.timestamp, orphanNodeIds)
    buildNodeChanges(loadedNodes)
  }

  private def buildNodeChanges(loadedNodes: Seq[LoadedNode]): Unit = {
    loadedNodes.foreach { loadedNode =>
      val location = config.nodeLocationAnalyzer.locate(loadedNode.node.latitude, loadedNode.node.longitude)
      config.changeSetRepository.saveNodeChange(
        NodeChangeAnalyzer.analyzed(
          NodeChange(
            key = config.changeSetContext.buildChangeKey(loadedNode.id),
            changeType = ChangeType.InitialValue,
            subsets = loadedNode.subsets,
            location = location,
            name = loadedNode.name,
            before = None,
            after = Some(loadedNode.node.raw),
            connectionChanges = Seq.empty,
            roleConnectionChanges = Seq.empty,
            definedInNetworkChanges = Seq.empty,
            tagDiffs = None,
            nodeMoved = None,
            addedToRoute = Seq.empty,
            removedFromRoute = Seq.empty,
            addedToNetwork = Seq.empty,
            removedFromNetwork = Seq.empty,
            factDiffs = FactDiffs(),
            facts = Seq.empty
          )
        )
      )
    }
  }
}
