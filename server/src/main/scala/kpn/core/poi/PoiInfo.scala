package kpn.core.poi

import kpn.shared.LatLon

case class PoiInfo(
  elementType: String,
  elementId: Long,
  latitude: String,
  longitude: String,
  layer: String
) extends LatLon
