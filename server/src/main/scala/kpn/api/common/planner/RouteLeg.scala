package kpn.api.common.planner

case class RouteLeg(
  legId: String,
  routes: Seq[RouteLegRoute]
)
