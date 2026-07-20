package kpn.api.common;

public record LatLonImpl(
  String latitude,
  String longitude
) implements LatLon {
}

/* TODO api migrate
package kpn.api.common

object LatLonImpl {
  def from(lat: Double, lon: Double): LatLonImpl = {
    LatLonImpl(lat.toString, lon.toString)
  }
}

*/
