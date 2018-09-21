package kpn.shared.node

case class NodeNetworkRouteReference(routeId: Long, routeName: String, routeRole: Option[String]) {
  def isConnection: Boolean = routeRole.contains("connection")
}
