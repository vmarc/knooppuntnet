package kpn.api.common.planner

import kpn.api.common.common.ToStringBuilder

case class Plan(
  sourceNode: PlanNode,
  legs: Seq[PlanLeg]
) {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("sourceNode", sourceNode).
    field("legs", legs).
    build

}
