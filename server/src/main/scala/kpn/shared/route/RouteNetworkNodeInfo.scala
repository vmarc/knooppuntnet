package kpn.shared.route

case class RouteNetworkNodeInfo(
  id: Long,
  name: String,
  alternateName: String = "",
  lat: String = "",
  lon: String = ""
)
