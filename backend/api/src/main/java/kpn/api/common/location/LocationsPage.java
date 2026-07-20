package kpn.api.common.location;

import kpn.api.common.location.LocationNode;

import java.util.Optional;

public record LocationsPage(
  Optional<LocationNode> locationNode
) {
}
