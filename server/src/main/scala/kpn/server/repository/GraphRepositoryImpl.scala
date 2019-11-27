package kpn.server.repository

import javax.annotation.PostConstruct
import kpn.api.custom.NetworkType
import kpn.core.database.Database
import kpn.core.database.views.planner.GraphEdgesView
import kpn.core.planner.graph.GraphEdge
import kpn.core.planner.graph.NodeNetworkGraph
import kpn.core.planner.graph.NodeNetworkGraphImpl
import kpn.core.util.Log
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpServerErrorException

import scala.annotation.tailrec

@Component
class GraphRepositoryImpl(
  analysisDatabase: Database,
  graphLoadEnabled: Boolean
) extends GraphRepository {

  private val log = Log(classOf[GraphRepositoryImpl])

  private var graphs: Map[String, NodeNetworkGraph] = Map()

  @PostConstruct
  def loadGraphs(): Unit = {
    if (graphLoadEnabled) {
      log.info("Loading graphs")
      graphs = NetworkType.all.map { networkType =>
        val graph = buildGraph(networkType)
        (networkType.name, graph)
      }.toMap
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
    val graph = new NodeNetworkGraphImpl()
    val edges = log.elapsed {
      val result = readEdges(networkType)
      (s"Loaded ${networkType.name} ${result.size} edges", result)
    }
    edges.foreach(graph.add)
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
