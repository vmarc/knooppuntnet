package kpn.api.common.route;

import java.util.Optional;

public record RouteNode(
  Long nodeId,
  String latitude,
  String longitude,
  String name,
  String alternateName,
  Optional<String> longName,
  Boolean isInWay
) {
}

/*
package kpn.api.common.route

import kpn.api.common.LatLon

case class RouteNode(
  nodeId: Long,
  latitude: String,
  longitude: String,
  name: String,
  alternateName: String,
  longName: Option[String],
  isInWay: Boolean,
) extends LatLon

*/
