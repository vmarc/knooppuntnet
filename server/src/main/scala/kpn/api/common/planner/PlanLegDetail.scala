package kpn.api.common.planner

import kpn.api.common.common.ToStringBuilder

case class PlanLegDetail(
  source: LegEnd,
  sink: LegEnd,
  routes: Seq[PlanRoute]
) {

  def meters: Long = routes.map(_.meters).sum

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("routes", routes).
    build

}
