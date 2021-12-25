package kpn.server.repository

import kpn.api.custom.NetworkType
import kpn.core.planner.graph.NodeNetworkGraph
import kpn.core.planner.graph.NodeNetworkGraphImpl
import kpn.core.util.Log
import kpn.database.actions.graph.MongoQueryGraphEdges
import kpn.database.base.Database
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

@Component
class GraphRepositoryImpl(
  database: Database,
  graphLoadEnabled: Boolean
) extends GraphRepository {

  private val log = Log(classOf[GraphRepositoryImpl])

  private var graphs: Map[String, NodeNetworkGraph] = Map.empty

  @PostConstruct
  def loadGraphs(): Unit = {
    if (graphLoadEnabled) {
      log.infoElapsed {
        graphs = {
          val graphEdges = new MongoQueryGraphEdges(database).execute()
          graphEdges.map { edges =>
            val graph = new NodeNetworkGraphImpl()
            edges.edges.foreach(graph.add)
            (edges.networkType.name, graph)
          }.toMap
        }
        ("Loading graphs", ())
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
}
