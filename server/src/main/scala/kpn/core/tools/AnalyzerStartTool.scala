package kpn.core.tools

import akka.actor.ActorSystem
import kpn.core.analysis.Network
import kpn.core.app.ActorSystemConfig
import kpn.core.common.TimestampUtil
import kpn.core.database.DatabaseImpl
import kpn.core.database.implementation.DatabaseContext
import kpn.core.db.couch.Couch
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.NetworkNodeBuilder
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.node.NodeChangeAnalyzer
import kpn.server.analyzer.engine.changes.route.RouteChangeAnalyzer
import kpn.server.analyzer.load.data.LoadedNode
import kpn.server.json.Json
import kpn.shared.Fact
import kpn.shared.NetworkType
import kpn.shared.ReplicationId
import kpn.shared.Subset
import kpn.shared.Timestamp
import kpn.shared.changes.ChangeSet
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NodeChange
import kpn.shared.changes.details.RouteChange
import kpn.shared.common.Ref
import kpn.shared.diff.common.FactDiffs
import kpn.shared.diff.route.RouteDiff
import kpn.shared.node.NodeNetworkTypeAnalyzer

import scala.concurrent.Await
import scala.concurrent.duration.Duration

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

        val system = ActorSystemConfig.actorSystem()
        try {
          val configuration = buildConfiguration(system, options)
          new AnalyzerStartTool(configuration).analyze()
        }
        finally {
          Await.result(system.terminate(), Duration.Inf)
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

  private def buildConfiguration(system: ActorSystem, options: AnalyzerStartToolOptions): AnalyzerStartToolConfiguration = {
    val couchConfig = Couch.config
    val analysisDatabase = new DatabaseImpl(DatabaseContext(couchConfig, Json.objectMapper, options.analysisDatabaseName))
    val changeDatabase = new DatabaseImpl(DatabaseContext(couchConfig, Json.objectMapper, options.changeDatabaseName))
    new AnalyzerStartToolConfiguration(
      system,
      analysisDatabase,
      changeDatabase
    )
  }
}

class AnalyzerStartTool(config: AnalyzerStartToolConfiguration) {

  private val log = AnalyzerStartTool.log

  def analyze(): Unit = {

    val replicationId = ReplicationId(1)
    val beginOsmChange = config.osmChangeRepository.get(replicationId)
    val timestamp = TimestampUtil.relativeSeconds(beginOsmChange.timestampUntil.get, -1)

    val context = ChangeSetContext(
      replicationId,
      ChangeSet(
        0,
        timestamp,
        timestamp,
        timestamp,
        timestamp,
        timestamp,
        Seq()
      )
    )

    config.analysisDataLoader.load(timestamp)

    processNetworks(context, timestamp)
    processOrphanRoutes(context, timestamp)
    processOrphanNodes(context, timestamp)
  }

  private def processNetworks(context: ChangeSetContext, timestamp: Timestamp): Unit = {

    val networkIds = config.analysisData.networks.watched.ids.toSeq
    networkIds.zipWithIndex.foreach { case (networkId, index) =>

      Log.context(s"network=$networkId ${index + 1}/${networkIds.size}") {
        log.unitElapsed {
          config.networkLoader.load(Some(timestamp), networkId) match {
            case Some(loadedNetwork) =>
              val networkRelationAnalysis = config.networkRelationAnalyzer.analyze(loadedNetwork.relation)
              log.info(s"""Analyze "${loadedNetwork.name}"""")
              val network: Network = config.networkAnalyzer.analyze(networkRelationAnalysis, loadedNetwork)

              network.routes.foreach { route =>
                config.changeSetRepository.saveRouteChange(
                  analyzed(
                    RouteChange(
                      key = context.buildChangeKey(route.id),
                      changeType = ChangeType.InitialValue,
                      name = route.routeAnalysis.route.summary.name,
                      addedToNetwork = Seq(Ref(network.id, network.name)),
                      removedFromNetwork = Seq.empty,
                      before = None,
                      after = Some(route.routeAnalysis.toRouteData),
                      removedWays = Seq.empty,
                      addedWays = Seq.empty,
                      updatedWays = Seq.empty,
                      diffs = RouteDiff(),
                      facts = route.routeAnalysis.route.facts
                    )
                  )
                )
              }

              network.nodes.foreach { node =>

                val networkTypes: Seq[NetworkType] = NodeNetworkTypeAnalyzer.networkTypes(node.networkNode.tags)
                val subsets: Seq[Subset] = node.networkNode.country.toSeq.flatMap { c => networkTypes.flatMap(n => Subset.of(c, n)) }
                val nodeChange = NodeChange(
                  key = context.buildChangeKey(node.id),
                  changeType = ChangeType.InitialValue,
                  subsets = subsets,
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
                  factDiffs = FactDiffs(),
                  facts = Seq.empty
                )
                config.changeSetRepository.saveNodeChange(analyzed(nodeChange))
              }
              loadedNetwork.name

            case None =>
              s"Failed to load network $networkId"
          }
        }
      }
    }
  }

  private def processOrphanRoutes(context: ChangeSetContext, timestamp: Timestamp): Unit = {

    val orphanRouteIds = config.analysisData.orphanRoutes.watched.ids.toSeq
    orphanRouteIds.zipWithIndex.foreach { case (routeId, index) =>
      Log.context(s"route $routeId ${index + 1}/${orphanRouteIds.size}") {

        config.routeLoader.loadRoute(timestamp, routeId) match {
          case None => log.warn("Could not load route")
          case Some(loadedRoute) =>
            val allNodes = new NetworkNodeBuilder(config.analysisContext, loadedRoute.data, loadedRoute.networkType, config.countryAnalyzer).networkNodes
            val analysis = config.routeAnalyzer.analyze(allNodes, loadedRoute, orphan = true)
            val facts = analysis.route.facts :+ Fact.OrphanRoute

            config.changeSetRepository.saveRouteChange(
              analyzed(
                RouteChange(
                  key = context.buildChangeKey(analysis.route.id),
                  changeType = ChangeType.InitialValue,
                  name = analysis.route.summary.name,
                  addedToNetwork = Seq.empty,
                  removedFromNetwork = Seq.empty,
                  before = None,
                  after = Some(analysis.toRouteData),
                  removedWays = Seq.empty,
                  addedWays = Seq.empty,
                  updatedWays = Seq.empty,
                  diffs = RouteDiff(),
                  facts = facts
                )
              )
            )
        }
      }
    }
  }

  private def processOrphanNodes(context: ChangeSetContext, timestamp: Timestamp): Unit = {

    val nodeIds = config.analysisData.orphanNodes.watched.ids.toSeq
    val loadedNodes: Seq[LoadedNode] = config.nodeLoader.loadNodes(timestamp, nodeIds)

    loadedNodes.foreach { loadedNode =>
      val nodeChange = NodeChange(
        key = context.buildChangeKey(loadedNode.id),
        changeType = ChangeType.InitialValue,
        subsets = loadedNode.subsets,
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
      config.changeSetRepository.saveNodeChange(analyzed(nodeChange))
    }
  }

  private def analyzed(routeChange: RouteChange): RouteChange = {
    new RouteChangeAnalyzer(routeChange).analyzed()
  }

  private def analyzed(nodeChange: NodeChange): NodeChange = {
    new NodeChangeAnalyzer(nodeChange).analyzed()
  }

}
