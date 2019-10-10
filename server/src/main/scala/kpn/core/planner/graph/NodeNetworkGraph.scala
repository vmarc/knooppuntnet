package kpn.core.planner.graph

trait NodeNetworkGraph {

  def add(edge: GraphEdge): Unit

  def findPath(sourceNodeId: Long, sinkNodeId: Long): Option[GraphPath]

}
