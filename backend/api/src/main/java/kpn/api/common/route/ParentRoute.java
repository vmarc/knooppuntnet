package kpn.api.common.route;

public record ParentRoute(
  Long level,
  Long routeId,
  String name
) {
}

/*
package kpn.api.common.route

case class ParentRoute(
  level: Long,
  routeId: Long,
  name: String
)

*/
