package kpn.server.repository

import kpn.api.custom.NetworkType
import kpn.core.database.views.planner.GraphEdgesView
import kpn.core.mongo.Database
import kpn.core.mongo.actions.graph.MongoQueryGraphEdges
import kpn.core.planner.graph.GraphEdge
import kpn.core.planner.graph.NodeNetworkGraph
import kpn.core.planner.graph.NodeNetworkGraphImpl
import kpn.core.util.Log
import kpn.core.util.Util
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpServerErrorException

import javax.annotation.PostConstruct
import scala.annotation.tailrec

@Component
class GraphRepositoryImpl(
  database: Database,
  analysisDatabase: kpn.core.database.Database,
  graphLoadEnabled: Boolean,
  mongoEnabled: Boolean
) extends GraphRepository {

  private val log = Log(classOf[GraphRepositoryImpl])

  private var graphs: Map[String, NodeNetworkGraph] = Map.empty

  @PostConstruct
  def loadGraphs(): Unit = {
    if (graphLoadEnabled) {
      log.infoElapsed("Loading graphs") {
        graphs = if (mongoEnabled) {
          val graphEdges = new MongoQueryGraphEdges(database).execute()
          graphEdges.map { edges =>
            val graph = new NodeNetworkGraphImpl()
            edges.edges.foreach(graph.add)
            (edges.networkType.name, graph)
          }.toMap
        }
        else {
          NetworkType.all.map { networkType =>
            val graph = buildGraph(networkType)
            (networkType.name, graph)
          }.toMap
        }
      }
    }
  }

  @Scheduled(initialDelay = 300000, fixedDelay = 300000) // 5 minutes = 5 * 60 * 1000 = 300000
  def reloadGraphs(): Unit = {
    loadGraphs()
  }

  override def graph(networkType: NetworkType): Option[NodeNetworkGraph] = {
    graphs.get(networkType.name)
  }

  private def buildGraph(networkType: NetworkType): NodeNetworkGraph = {

    val memoryBefore = Util.memoryUsed()

    val graph = new NodeNetworkGraphImpl()
    val edges = log.elapsed {
      val result = readEdges(networkType)
      (s"Loaded ${networkType.name} ${result.size} edges", result)
    }
    edges.foreach(graph.add)

    val memoryAfter = Util.memoryUsed()
    log.info(s"Loaded ${networkType.name} memory: ${Util.humanReadableBytes(memoryAfter - memoryBefore)}")

    graph
  }

  @tailrec
  private def readEdges(networkType: NetworkType): Seq[GraphEdge] = {
    try {
      GraphEdgesView.query(analysisDatabase, networkType, stale = false)
    }
    catch {
      case e: HttpServerErrorException =>
        if (e.getResponseBodyAsString.contains("The request could not be processed in a reasonable amount of time")) {
          log.info("Continue waiting for indexing to finish")
        }
        else {
          log.info("Continue waiting for indexing to finish (Exception) " + e.getMessage)
        }
        readEdges(networkType)
    }
  }
}
