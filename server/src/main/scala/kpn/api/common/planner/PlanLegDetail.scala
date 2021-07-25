package kpn.api.common.planner

case class PlanLegDetail(
  source: LegEnd,
  sink: LegEnd,
  routes: Seq[PlanRoute]
) {
  def meters: Long = routes.map(_.meters).sum
}
