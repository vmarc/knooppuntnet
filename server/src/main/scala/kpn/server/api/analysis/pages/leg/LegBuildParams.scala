package kpn.server.api.analysis.pages.leg

import kpn.api.custom.NetworkType

case class LegBuildParams(
  networkType: NetworkType,
  legId: String,
  sourceNodeId: Long,
  sinkNodeId: Long,
  viaRoute: Option[ViaRoute]
)
