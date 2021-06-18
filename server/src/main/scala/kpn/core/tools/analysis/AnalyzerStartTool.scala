package kpn.core.tools.analysis

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
import kpn.core.analysis.NetworkNode
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.overpass.QueryNode
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.engine.changes.changes.RouteElements
import kpn.server.analyzer.engine.changes.node.NodeChangeAnalyzer
import kpn.server.analyzer.engine.changes.route.RouteChangeAnalyzer
import kpn.server.analyzer.load.data.LoadedNode
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

import java.util.concurrent.CompletableFuture.allOf
import java.util.concurrent.CompletableFuture.supplyAsync
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy
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

  private val tempBlacklistedRouteIds = { // TODO remove this temporary code (blacklist speeds up initial processing during test phase)
    val source = Source.fromFile("/kpn/conf/temp-blacklist.txt")
    val res = source.getLines().map(_.toLong).toSet
    source.close()
    res
  }

  def analyze(): Unit = {
    loadNetworks()
    loadOrphanRoutes()
    loadOrphanNodes()
  }

  private def loadNetworks(): Unit = {
    Log.context("networks") {
      val networkIds = config.analysisContext.snapshotKnownElements.networkIds.toSeq.sorted
      log.info(s"Trying to load a maximum of ${networkIds.size} networks (using networkIds in snapshot)")
      val futures = networkIds.zipWithIndex.map { case (networkId, index) =>
        val context = Log.contextAnd(s"${index + 1}/${networkIds.size}, network=$networkId")
        supplyAsync(() => Log.context(context)(loadNetwork(networkId)), config.analysisExecutor)
      }
      allOf(futures: _*).join()
      log.info(s"${config.analysisContext.data.networks.watched.size} networks loaded")
    }
  }

  private def loadNetwork(networkId: Long): Unit = {
    try {
      config.networkLoader.load(Some(config.timestamp), networkId) match {
        case None => log.error(s"Failed to load network $networkId")
        case Some(loadedNetwork) =>
          log.info(s"""Analyze "${loadedNetwork.name}"""")
          val networkRelationAnalysis = config.networkRelationAnalyzer.analyze(loadedNetwork.relation)
          val network = config.networkAnalyzer.analyze(networkRelationAnalysis, loadedNetwork)
          config.analysisRepository.saveNetwork(network)
          config.analysisContext.data.networks.watched.add(loadedNetwork.networkId, networkRelationAnalysis.elementIds)
          loadNetworkChange(network)
          loadNetworkRouteChanges(network)
          loadNetworkNodeChanges(network)
      }
    }
    catch {
      case e: Exception =>
        val message = s"Could not load network $networkId"
        log.error(message, e)
        throw new RuntimeException(message, e)
    }
  }

  private def loadOrphanRoutes(): Unit = {
    config.analysisDatabaseIndexer.index(true)
    Log.context("orphan-routes") {
      val allRouteIds = config.routeRepository.allRouteIds()
      val orphanRouteIds = config.analysisContext.snapshotKnownElements.routeIds -- allRouteIds.toSet -- tempBlacklistedRouteIds
      log.info(s"Trying to load a maximum of ${orphanRouteIds.size} orphan routes")
      val futures = orphanRouteIds.toSeq.sorted.zipWithIndex.map { case (routeId, index) =>
        val context = Log.contextAnd(s"${index + 1}/${orphanRouteIds.size}, route=$routeId")
        supplyAsync(() => Log.context(context)(loadOrphanRoute(routeId)), config.analysisExecutor)
      }
      allOf(futures: _*).join()
      log.info(s"${config.analysisContext.data.orphanRoutes.watched.size} orphan routes loaded")
    }
  }

  private def loadOrphanRoute(routeId: Long): Unit = {

    val loadedRouteOption = config.routeLoader.loadRoute(config.timestamp, routeId)
    loadedRouteOption match {
      case None => log.info(s"Could not load route $routeId")
      case Some(loadedRoute) =>

        val analysis = config.routeAnalyzer.analyze(loadedRoute, orphan = true)
        val route = analysis.route.copy(orphan = true)
        config.routeRepository.save(route)
        config.routeRepository.saveElements(
          RouteElements(
            loadedRoute.id,
            config.relationAnalyzer.toElementIds(analysis.relation)
          )
        )
        loadOrphanRouteChange(analysis)

        val allNodes = config.networkNodeAnalyzer.analyze(loadedRoute.scopedNetworkType, loadedRoute.data)

        allNodes.values.foreach { networkNode =>
          config.nodeRepository.save(
            config.nodeInfoBuilder.build(
              id = networkNode.id,
              active = true,
              orphan = false,
              country = networkNode.country,
              latitude = networkNode.node.latitude,
              longitude = networkNode.node.longitude,
              lastUpdated = networkNode.node.timestamp,
              tags = networkNode.node.tags,
              facts = Seq()
            )
          )
          loadNodeChange(networkNode)
        }

        val elementIds = config.relationAnalyzer.toElementIds(loadedRoute.relation)
        config.analysisContext.data.orphanRoutes.watched.add(loadedRoute.id, elementIds)
    }
  }

  private def loadOrphanNodes(): Unit = {
    config.analysisDatabaseIndexer.index(true)
    val allNodeIds = config.nodeRepository.allNodeIds()
    val orphanNodeIds = config.analysisContext.snapshotKnownElements.nodeIds -- allNodeIds
    Log.context("orphan-nodes") {
      log.info(s"Trying to load a maximum of ${orphanNodeIds.size} orphan nodes")
      orphanNodeIds.toSeq.sorted.zipWithIndex.foreach { case (nodeId, index) =>
        Log.context(s"${index + 1}/${orphanNodeIds.size}, node=$nodeId") {
          val xmlString = config.nonCachingExecutor.executeQuery(Some(config.timestamp), QueryNode(nodeId))
          val xml = XML.loadString(xmlString)
          val rawData = new Parser().parse(xml.head)
          val data = new DataBuilder(rawData).data
          data.nodes.get(nodeId) match {
            case None => log.error("Node not found")
            case Some(node) =>
              val countries = config.countryAnalyzer.countries(node)
              val networkTypes = NetworkType.all.filter { networkType =>
                config.analysisContext.isValidNetworkNode(networkType, node.raw)
              }
              val name = config.nodeAnalyzer.name(node.tags)
              val longName = config.nodeAnalyzer.longName(node.tags)
              val loadedNode = LoadedNode(countries.headOption, networkTypes, name, node)
              val nodeInfo = config.nodeInfoBuilder.fromLoadedNode(loadedNode, orphan = true)
              config.analysisContext.data.orphanNodes.watched.add(nodeId)
              config.nodeRepository.save(nodeInfo)
              val networkNode = NetworkNode(
                loadedNode.node,
                loadedNode.name,
                longName,
                loadedNode.country,
                nodeInfo.location
              )
              loadNodeChange(networkNode)
          }
        }
      }
      log.info(s"${config.analysisContext.data.orphanNodes.watched.size} orphan nodes loaded")
    }
  }

  private def loadNetworkChange(network: Network): Unit = {

    val nodeRefs = network.nodes.map(node => Ref(node.id, node.networkNode.name))
    val routeRefs = network.routes.map(route => Ref(route.id, route.routeAnalysis.name))
    val key = config.changeSetContext.buildChangeKey(network.id)
    config.changeSetRepository.saveNetworkChange(
      NetworkChange(
        _id = key.toId,
        key = key,
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
        investigate = network.facts.nonEmpty,
        impact = true
      )
    )
  }

  private def loadNetworkRouteChanges(network: Network): Unit = {

    network.routes.foreach { networkMemberRoute =>

      val factDiffs = if (networkMemberRoute.routeAnalysis.route.facts.nonEmpty) {
        Some(FactDiffs(remaining = networkMemberRoute.routeAnalysis.route.facts.toSet))
      }
      else {
        None
      }

      val key = config.changeSetContext.buildChangeKey(networkMemberRoute.id)
      config.changeSetRepository.saveRouteChange(
        RouteChangeAnalyzer.analyzed(
          RouteChange(
            _id = key.toId,
            key = key,
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

  private def loadNetworkNodeChanges(network: Network): Unit = {

    network.nodes.foreach { node =>

      val subsets: Seq[Subset] = {
        val networkTypes = NetworkType.all.filter { networkType =>
          config.analysisContext.isValidNetworkNode(networkType, node.networkNode.node.raw)
        }
        node.networkNode.country.toSeq.flatMap {
          c => networkTypes.flatMap(n => Subset.of(c, n))
        }
      }

      val key = config.changeSetContext.buildChangeKey(node.id)
      config.changeSetRepository.saveNodeChange(
        NodeChangeAnalyzer.analyzed(
          NodeChange(
            _id = key.toId,
            key = key,
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
      )
    }
  }

  private def loadOrphanRouteChange(analysis: RouteAnalysis): Unit = {

    val facts = analysis.route.facts :+ Fact.OrphanRoute

    val key = config.changeSetContext.buildChangeKey(analysis.route.id)
    config.changeSetRepository.saveRouteChange(
      RouteChangeAnalyzer.analyzed(
        RouteChange(
          _id = key.toId,
          key = key,
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
          diffs = RouteDiff(factDiffs = Some(FactDiffs(remaining = facts.toSet))),
          facts = facts
        )
      )
    )
  }

  private def loadNodeChange(networkNode: NetworkNode): Unit = {

    def subsets: Seq[Subset] = {
      networkNode.country match {
        case None => Seq()
        case Some(c) =>
          val networkTypes = NetworkType.all.filter { networkType =>
            config.analysisContext.isValidNetworkNode(networkType, networkNode.node.raw)
          }
          networkTypes.map(n => Subset(c, n))
      }
    }

    val key = config.changeSetContext.buildChangeKey(networkNode.id)
    config.changeSetRepository.saveNodeChange(
      NodeChangeAnalyzer.analyzed(
        NodeChange(
          _id = key.toId,
          key = key,
          changeType = ChangeType.InitialValue,
          subsets = subsets,
          location = networkNode.location,
          name = networkNode.name,
          before = None,
          after = Some(networkNode.node.raw),
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
