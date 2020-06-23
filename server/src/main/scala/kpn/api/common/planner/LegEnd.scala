package kpn.api.common.planner

import kpn.api.common.common.ToStringBuilder

object LegEnd {

  def node(nodeId: Long): LegEnd = {
    LegEnd(Some(LegEndNode(nodeId)), None)
  }

  def route(routeId: Long, pathId: Long): LegEnd = {
    route(LegEndRoute(routeId, pathId))
  }

  def route(legEndRoute: LegEndRoute): LegEnd = {
    LegEnd(None, Some(legEndRoute))
  }

}

case class LegEnd(
  node: Option[LegEndNode],
  route: Option[LegEndRoute]
) {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("node", node).
    field("route", route).
    build

}
