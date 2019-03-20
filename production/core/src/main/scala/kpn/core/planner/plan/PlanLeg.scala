package kpn.core.planner.plan

case class PlanLeg(sink: PlanNode, fragments: Seq[PlanLegFragment])
