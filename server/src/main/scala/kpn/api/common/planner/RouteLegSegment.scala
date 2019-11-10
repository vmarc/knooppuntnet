package kpn.api.common.planner

case class RouteLegSegment(
  meters: Int,
  surface: String,
  fragments: Seq[RouteLegFragment]
)
