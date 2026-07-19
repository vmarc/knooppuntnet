package kpn.api.common.location;

import kpn.api.common.location.LocationNode;

import java.util.Optional;

public record LocationsPage(
  Optional<LocationNode> locationNode
) {
}

/*
package kpn.api.common.location

case class LocationsPage(locationNode: Option[LocationNode])

*/
