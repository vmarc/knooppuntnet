package kpn.api.common.route

case class RouteNetworkNodeInfo(
  id: Long,
  name: String,
  alternateName: String = "",
  longName: Option[String] = None,
  lat: String = "",
  lon: String = ""
)
