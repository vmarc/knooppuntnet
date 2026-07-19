package kpn.api.common;

public interface LatLon {

  String latitude();

  String longitude();

  default double lat() {
    return Double.parseDouble(latitude());
  }

  default double lon() {
    return Double.parseDouble(longitude());
  }
}
