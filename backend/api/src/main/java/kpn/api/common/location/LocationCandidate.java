package kpn.api.common.location;

import kpn.api.common.location.Location;

public record LocationCandidate(
  Location location,
  Long percentage
) {
}

/*
package kpn.api.common.location

case class LocationCandidate(location: Location, percentage: Long)

*/
