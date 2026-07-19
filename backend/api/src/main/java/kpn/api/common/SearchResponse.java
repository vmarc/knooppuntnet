package kpn.api.common;

import kpn.api.common.GeocoderLocation;

import com.google.common.collect.ImmutableList;

public record SearchResponse(
  ImmutableList<GeocoderLocation> geocoderLocations
) {
}

/*
package kpn.api.common

case class SearchResponse(
  geocoderLocations: Seq[GeocoderLocation]
)

*/
