package kpn.api.common.planner

case class RouteLegSegment(
  meters: Long,
  surface: String,
  colour: Option[String],
  fragments: Seq[RouteLegFragment]
)
