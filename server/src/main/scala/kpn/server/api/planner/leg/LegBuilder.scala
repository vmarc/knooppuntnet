package kpn.server.api.planner.leg

import kpn.api.common.planner.LegBuildParams
import kpn.api.common.planner.RouteLeg
import kpn.api.custom.NetworkType

trait LegBuilder {

  def build(params: LegBuildParams): Option[RouteLeg]

  def load(networkType: NetworkType, planString: String, encoded: Boolean = true): Option[Seq[RouteLeg]]
}
