package kpn.api.common.node

import kpn.api.common.common.Ref
import kpn.api.custom.Timestamp

case class MapNodeDetail(
  id: Long,
  name: String,
  latitude: String,
  longitude: String,
  lastUpdated: Timestamp,
  networkReferences: Seq[Ref],
  routeReferences: Seq[Ref]
)
