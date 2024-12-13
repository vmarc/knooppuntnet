package kpn.api.common

case class NodeMapInfo(
  id: Long,
  name: String,
  networkTypes: Seq[NetworkType],
  latitude: String,
  longitude: String
)
