package kpn.api.common.planner

case class RouteLegRoute(
  source: RouteLegNode,
  sink: RouteLegNode,
  meters: Int,
  segments: Seq[RouteLegSegment],
  streets: Seq[String]
)
