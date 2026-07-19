package kpn.api.common.poi;

import kpn.api.common.location.LocationNode;

import java.util.Optional;

public record PoiLocationsPage(
  Optional<LocationNode> locationNode
) {
}

/*
package kpn.api.common.poi

import kpn.api.common.location.LocationNode

case class PoiLocationsPage(
  locationNode: Option[LocationNode]
)

*/
