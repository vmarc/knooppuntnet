package kpn.api.common.planner

case class PlanSegment(
  meters: Long,
  surface: String,
  colour: String,
  fragments: List[PlanFragment]
)
