package kpn.api.common.route;

import java.util.Optional;

public record RouteNetworkNodeInfo(
  Long id,
  String name,
  String alternateName,
  Optional<String> longName,
  String lat,
  String lon
) {
}

/*
package kpn.api.common.route

case class RouteNetworkNodeInfo(
  id: Long,
  name: String,
  alternateName: String,
  longName: Option[String],
  lat: String,
  lon: String
)

*/
