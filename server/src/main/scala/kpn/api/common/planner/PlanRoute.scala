package kpn.api.common.planner

case class PlanRoute(
  sourceNode: PlanNode,
  sinkNode: PlanNode,
  meters: Long,
  segments: List[PlanSegment],
  streets: List[String]
)
