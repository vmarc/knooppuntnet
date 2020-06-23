package kpn.api.common.planner

import kpn.api.common.common.ToStringBuilder

case class LegEndRoute(routeId: Long, pathId: Long) {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("routeId", routeId).
    field("pathId", pathId).
    build

}
