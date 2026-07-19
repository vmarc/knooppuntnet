package kpn.api.common.route;

import kpn.api.common.ElementChangeType;

public record RouteNodeChange(
  Long id,
  String latitude,
  String longitude,
  ElementChangeType changeType
) {
}

/*
package kpn.api.common.route

import kpn.api.common.ElementChangeType

case class RouteNodeChange(
  id: Long,
  latitude: String,
  longitude: String,
  changeType: ElementChangeType
)

*/
