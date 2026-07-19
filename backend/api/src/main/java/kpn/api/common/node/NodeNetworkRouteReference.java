package kpn.api.common.node;

import java.util.Optional;

public record NodeNetworkRouteReference(
  Long routeId,
  String routeName,
  Optional<String> routeRole
) {
}

/*
package kpn.api.common.node

case class NodeNetworkRouteReference(routeId: Long, routeName: String, routeRole: Option[String]) {
  def isConnection: Boolean = routeRole.contains("connection")
}

*/
