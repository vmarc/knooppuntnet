package kpn.server.api.analysis.pages.leg

import kpn.api.common.planner.RouteLeg

trait LegBuilder {
  def build(params: LegBuildParams): Option[RouteLeg]
}
