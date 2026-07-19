package kpn.api.common.location;

import kpn.api.common.Bounds;
import kpn.api.common.location.LocationSummary;

public record LocationMapPage(
  LocationSummary summary,
  Bounds bounds,
  String geoJson,
  String geoJson2
) {
}

/*
package kpn.api.common.location

import kpn.api.common.Bounds

case class LocationMapPage(
  summary: LocationSummary,
  bounds: Bounds,
  geoJson: String,
  geoJson2: String
)

*/
