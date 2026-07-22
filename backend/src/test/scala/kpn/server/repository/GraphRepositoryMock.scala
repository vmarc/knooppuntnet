package kpn.server.repository

import kpn.api.common.RouteType
import kpn.core.planner.graph.NodeNetworkGraph

import scala.collection.mutable

class GraphRepositoryMock extends GraphRepository {

  private val graphs = mutable.Map[RouteType, NodeNetworkGraph]()

  override def graph(routeType: RouteType): Option[NodeNetworkGraph] = {
    graphs.get(routeType)
  }

  def addGraph(routeType: RouteType, graph: NodeNetworkGraph): Unit = {
    graphs.put(routeType, graph)
  }
}
