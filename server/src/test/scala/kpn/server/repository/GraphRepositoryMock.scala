package kpn.server.repository

import kpn.api.custom.NetworkType
import kpn.core.planner.graph.NodeNetworkGraph

import scala.collection.mutable

class GraphRepositoryMock extends GraphRepository {

  private val graphs = mutable.Map[NetworkType, NodeNetworkGraph]()

  override def graph(networkType: NetworkType): Option[NodeNetworkGraph] = {
    graphs.get(networkType)
  }

  def addGraph(networkType: NetworkType, graph: NodeNetworkGraph): Unit = {
    graphs.put(networkType, graph)
  }
}
