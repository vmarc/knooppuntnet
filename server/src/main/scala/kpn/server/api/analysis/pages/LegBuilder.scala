package kpn.server.api.analysis.pages

import kpn.api.common.planner.RouteLeg
import kpn.api.custom.NetworkType

trait LegBuilder {
  def build(networkType: NetworkType, legId: String, sourceNodeId: String, sinkNodeId: String): Option[RouteLeg]
}
