package kpn.api.common.poi;

import kpn.api.common.location.LocationNode;

import java.util.Optional;

public record PoiLocationsPage(
  Optional<LocationNode> locationNode
) {}
