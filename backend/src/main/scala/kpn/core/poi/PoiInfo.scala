package kpn.core.poi

import kpn.api.common.LatLon
import kpn.api.id.Storable

case class PoiInfo(
  elementType: String,
  elementId: Long,
  latitude: String,
  longitude: String,
  layer: String
) extends LatLon with Storable
