package kpn.api.common

case class NodeMapInfo(
  id: Long,
  name: String,
  routeTypes: Seq[RouteType],
  latitude: String,
  longitude: String
)
