package kpn.api.common.planner

case class PlanRoute(
  sourceNode: PlanNode,
  sinkNode: PlanNode,
  meters: Long,
  segments: Seq[PlanSegment],
  streets: Seq[String]
)
