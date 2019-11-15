package kpn.api.common.planner

case class RouteLegRoute(
  source: RouteLegNode,
  sink: RouteLegNode,
  meters: Long,
  segments: Seq[RouteLegSegment],
  streets: Seq[String]
)
