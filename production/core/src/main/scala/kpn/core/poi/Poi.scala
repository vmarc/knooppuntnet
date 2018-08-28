package kpn.core.poi

import kpn.shared.LatLon
import kpn.shared.data.Tags

case class Poi(
  elementType: String,
  elementId: Long,
  latitude: String,
  longitude: String,
  layers: Seq[String],
  tags: Tags
) extends LatLon
