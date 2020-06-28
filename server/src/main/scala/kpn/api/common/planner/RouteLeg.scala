package kpn.api.common.planner

import kpn.api.common.common.ToStringBuilder

case class RouteLeg(
  legId: String,
  routes: Seq[RouteLegRoute]
) {

  def meters: Long = routes.map(_.meters).sum

  def allNodeIds: Set[Long] = routes.flatMap(_.allNodeIds).toSet

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("legId", legId).
    field("routes", routes).
    build

}
