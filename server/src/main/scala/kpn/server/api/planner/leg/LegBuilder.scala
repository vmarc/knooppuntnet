package kpn.server.api.planner.leg

import kpn.api.common.NetworkType
import kpn.api.common.planner.LegBuildParams
import kpn.api.common.planner.PlanLegDetail

trait LegBuilder {

  def leg(params: LegBuildParams): Option[PlanLegDetail]

  def plan(networkType: NetworkType, planString: String, encoded: Boolean = true, proposed: Boolean): Option[Seq[PlanLegDetail]]
}
