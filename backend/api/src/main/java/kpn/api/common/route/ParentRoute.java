package kpn.api.common.route;

public record ParentRoute(
  Long level,
  Long routeId,
  String name
) {
}
