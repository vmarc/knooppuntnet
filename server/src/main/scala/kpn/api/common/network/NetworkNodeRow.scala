package kpn.api.common.network

import kpn.api.common.common.Reference

case class NetworkNodeRow(
  detail: NetworkNodeDetail,
  routeReferences: Seq[Reference]
)
