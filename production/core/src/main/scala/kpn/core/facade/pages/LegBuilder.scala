package kpn.core.facade.pages

import kpn.shared.planner.RouteLeg

trait LegBuilder {
  def build(networkType: String, legId: String, sourceNodeId: String, sinkNodeId: String): Option[RouteLeg]
}
