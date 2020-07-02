package kpn.server.api.planner.leg

import kpn.api.common.planner.Plan
import kpn.api.common.planner.PlanLeg

class PlanReducer {

  def reduce(plan: Plan): Plan = {

    val featureId = plan.legs.head.featureId
    val source = plan.legs.head.source
    val sink = plan.legs.last.sink
    val key = PlanKey.leg(source, sink)
    val sourceNode = plan.legs.head.sourceNode
    val sinkNode = plan.legs.last.sinkNode
    val meters = plan.legs.map(_.meters).sum
    val allRoutes = plan.legs.flatMap(_.routes)

    val planLeg = PlanLeg(
      featureId,
      key,
      source,
      sink,
      sourceNode,
      sinkNode,
      meters,
      allRoutes
    )

    Plan(
      sourceNode,
      Seq(planLeg)
    )
  }

}
