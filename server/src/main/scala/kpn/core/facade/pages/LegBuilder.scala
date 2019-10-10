package kpn.core.facade.pages

import kpn.shared.NetworkType
import kpn.shared.planner.RouteLeg

trait LegBuilder {
  def build(networkType: NetworkType, legId: String, sourceNodeId: String, sinkNodeId: String): Option[RouteLeg]
}
