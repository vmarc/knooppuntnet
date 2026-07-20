package kpn.api.common.node;

import java.util.Optional;

public record NodeNetworkRouteReference(
  Long routeId,
  String routeName,
  Optional<String> routeRole
) {}

/* TODO migrate

  def isConnection: Boolean = routeRole.contains("connection")

*/
