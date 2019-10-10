package kpn.shared.planner

case class RouteLeg(
  legId: String,
  routes: Seq[RouteLegRoute]
)
