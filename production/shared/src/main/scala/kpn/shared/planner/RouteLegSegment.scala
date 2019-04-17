package kpn.shared.planner

case class RouteLegSegment(
  meters: Int,
  surface: String,
  fragments: Seq[RouteLegFragment]
)
