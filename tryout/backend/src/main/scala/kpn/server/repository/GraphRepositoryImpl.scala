package kpn.server.repository

import kpn.api.common.RouteType
import kpn.core.planner.graph.NodeNetworkGraph
import kpn.core.util.Log
import kpn.database.actions.graph.MongoQueryGraphEdges
import kpn.database.base.Database
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

@Component
@Profile(Array("web"))
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
            val graph = new NodeNetworkGraph()
            edges.edges.foreach(graph.add)
            (edges.routeType.entryName, graph)
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

  override def graph(routeType: RouteType): Option[NodeNetworkGraph] = {
    graphs.get(routeType.entryName)
  }
}
