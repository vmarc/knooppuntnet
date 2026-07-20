package kpn.api.common;

import kpn.api.common.GeocoderLocation;

import com.google.common.collect.ImmutableList;

public record SearchResponse(
  ImmutableList<GeocoderLocation> geocoderLocations
) {
}
