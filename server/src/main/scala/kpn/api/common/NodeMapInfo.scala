package kpn.api.common

import kpn.api.custom.NetworkType

case class NodeMapInfo(
  id: Long,
  name: String,
  networkTypes: Seq[NetworkType],
  latitude: String,
  longitude: String
)
