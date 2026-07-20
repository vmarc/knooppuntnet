package kpn.api.common;

import kpn.api.common.location.Location;
import kpn.api.common.location.LocationCandidate;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record RouteLocationAnalysis(
  Optional<Location> location,
  ImmutableList<LocationCandidate> candidates,
  ImmutableList<String> locationNames
) {
}
