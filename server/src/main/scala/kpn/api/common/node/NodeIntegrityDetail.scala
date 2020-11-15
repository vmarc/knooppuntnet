package kpn.api.common.node

import kpn.api.common.common.Ref
import kpn.api.custom.NetworkType

case class NodeIntegrityDetail(
  networkType: NetworkType,
  expectedRouteCount: Int,
  routeRefs: Seq[Ref]
)