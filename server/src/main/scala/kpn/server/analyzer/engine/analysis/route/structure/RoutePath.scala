package kpn.server.analyzer.engine.analysis.route.structure

case class RoutePath(
  id: Long,
  direction: RoutePathDirection,
  elements: Seq[NewRouteSegmentElement]
) {

  if (elements.isEmpty || elements.size > 1) {
    throw new RuntimeException("UNEXPECTED !!!")
  }

  def nodeIds: Seq[Long] = {
    val ids: Seq[Long] = elements.headOption match {
      case Some(firstElement) => firstElement.nodeIds ++ elements.tail.flatMap(_.nodeIds)
      case None => Seq.empty
    }
    if (direction == RoutePathDirection.Backward) {
      ids.reverse
    }
    else {
      ids
    }
  }

  def fromNodeId: Long = {
    if (direction == RoutePathDirection.Backward) {
      elements.last.toNodeId
    }
    else {
      elements.head.fromNodeId
    }
  }

  def toNodeId: Long = {
    if (direction == RoutePathDirection.Backward) {
      elements.head.fromNodeId
    }
    else {
      elements.last.toNodeId
    }
  }
}
