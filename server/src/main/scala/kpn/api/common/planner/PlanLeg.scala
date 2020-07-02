package kpn.api.common.planner

import kpn.api.common.common.ToStringBuilder

case class PlanLeg(
  featureId: String,
  key: String,
  source: LegEnd,
  sink: LegEnd,
  sourceNode: PlanNode,
  sinkNode: PlanNode,
  meters: Long,
  routes: Seq[PlanRoute]
) {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("featureId", featureId).
    field("key", key).
    field("source", source).
    field("sink", sink).
    field("sourceNode", sourceNode).
    field("sinkNode", sinkNode).
    field("meters", meters).
    field("routes", routes).
    build

}
