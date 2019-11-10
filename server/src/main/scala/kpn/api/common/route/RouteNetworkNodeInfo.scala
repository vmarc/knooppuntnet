package kpn.api.common.route

case class RouteNetworkNodeInfo(
  id: Long,
  name: String,
  alternateName: String = "",
  lat: String = "",
  lon: String = ""
)
