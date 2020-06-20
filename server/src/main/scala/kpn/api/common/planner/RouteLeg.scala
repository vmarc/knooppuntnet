package kpn.api.common.planner

case class RouteLeg(
  legId: String,
  routes: Seq[RouteLegRoute]
) {
  def meters: Long = routes.map(_.meters).sum
}
