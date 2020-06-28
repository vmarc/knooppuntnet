package kpn.core.planner.graph

trait NodeNetworkGraph {

  def add(edge: GraphEdge): Unit

  def findPath(source: String, sink: String): Option[GraphPath]

}
