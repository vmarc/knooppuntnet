package kpn.api.common

case class GeocoderLocation(
  name: String,
  latitude: String,
  longitude: String,
  bounds: Bounds
)
