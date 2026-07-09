package kpn.api.common.route

import kpn.api.common.common.Ref

case class MapRouteDetail(
  id: Long,
  name: String,
  networkReferences: Seq[Ref]
)
