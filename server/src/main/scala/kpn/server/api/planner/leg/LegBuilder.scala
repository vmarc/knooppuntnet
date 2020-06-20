package kpn.server.api.planner.leg

import kpn.api.common.planner.RouteLeg

trait LegBuilder {
  def build(params: LegBuildParams): Option[RouteLeg]
}
