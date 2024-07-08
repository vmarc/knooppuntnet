package kpn.server.analyzer.engine.analysis.route.structure

import kpn.server.analyzer.engine.analysis.route.RouteNodeData

case class NewRouteSegmentElement(
  id: Long,
  direction: RoutePathDirection,
  fromNetworkNode: Option[RouteNodeData],
  toNetworkNode: Option[RouteNodeData],
  fromNodeId: Long,
  toNodeId: Long,
  fragments: Seq[NewRouteSegmentElementFragment]
) {
  def nodeIds: Seq[Long] = {
    val ids = fragments.headOption match {
      case Some(firstLink) => firstLink.nodeIds ++ fragments.tail.flatMap(link => link.nodeIds.tail)
      case None => Seq.empty
    }
    if (direction == RoutePathDirection.Backward) {
      ids.reverse
    }
    else {
      ids
    }
  }
}
