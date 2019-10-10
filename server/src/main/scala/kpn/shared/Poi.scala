package kpn.shared

import kpn.shared.data.Tags

case class Poi(
  elementType: String,
  elementId: Long,
  latitude: String,
  longitude: String,
  layers: Seq[String],
  tags: Tags
) extends LatLon
