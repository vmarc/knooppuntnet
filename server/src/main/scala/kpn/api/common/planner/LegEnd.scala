package kpn.api.common.planner

object LegEnd {

  def node(nodeId: Long): LegEnd = {
    LegEnd(Some(LegEndNode(nodeId)), None)
  }

  def route(routeId: Long, pathId: Long): LegEnd = {
    LegEnd(None, Some(LegEndRoute(routeId, pathId)))
  }

}

case class LegEnd(
  node: Option[LegEndNode],
  route: Option[LegEndRoute]
)
