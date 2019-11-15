package kpn.api.common.planner

case class RouteLegSegment(
  meters: Long,
  surface: String,
  fragments: Seq[RouteLegFragment]
)
