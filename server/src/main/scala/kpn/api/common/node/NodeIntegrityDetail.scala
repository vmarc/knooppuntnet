package kpn.api.common.node

import kpn.api.common.common.Ref
import kpn.api.custom.{NetworkScope, NetworkType}

case class NodeIntegrityDetail(
  networkScope: NetworkScope,
  networkType: NetworkType,
  expectedRouteCount: Int,
  routeRefs: Seq[Ref]
)
