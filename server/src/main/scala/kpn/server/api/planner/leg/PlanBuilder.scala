package kpn.server.api.planner.leg

import kpn.api.common.planner.LegBuildParams
import kpn.api.common.planner.Plan
import kpn.api.common.planner.PlanLeg
import kpn.api.custom.NetworkType

trait PlanBuilder {

  def leg(params: LegBuildParams): Option[PlanLeg]

  def plan(networkType: NetworkType, planString: String, encoded: Boolean = true): Option[Plan]
}
