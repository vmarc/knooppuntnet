package kpn.api.common.node

import kpn.api.common.common.Ref
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType

case class NodeIntegrityDetail(
  networkType: NetworkType,
  networkScope: NetworkScope,
  expectedRouteCount: Int,
  routeRefs: Seq[Ref]
)
