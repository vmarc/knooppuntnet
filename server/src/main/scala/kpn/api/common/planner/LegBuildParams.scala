package kpn.api.common.planner

import kpn.api.custom.NetworkType

case class LegBuildParams(
  networkType: String,
  legId: String,
  sourceNodeId: Long,
  sinkNodeId: Long,
  viaRoute: Option[ViaRoute]
)
