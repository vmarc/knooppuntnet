package kpn.server.api.planner.leg

import kpn.api.custom.NetworkType

case class LegBuildParams(
  networkType: NetworkType,
  legId: String,
  sourceNodeId: Long,
  sinkNodeId: Long,
  viaRoute: Option[ViaRoute]
)
