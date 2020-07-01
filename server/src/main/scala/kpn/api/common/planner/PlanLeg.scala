package kpn.api.common.planner

case class PlanLeg(
  featureId: String,
  key: String,
  source: LegEnd,
  sink: LegEnd,
  sourceNode: PlanNode,
  sinkNode: PlanNode,
  meters: Long,
  routes: Seq[PlanRoute]
)
