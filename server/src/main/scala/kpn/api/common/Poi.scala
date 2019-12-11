package kpn.api.common

import kpn.api.custom.Tags

case class Poi(
  elementType: String,
  elementId: Long,
  latitude: String,
  longitude: String,
  layers: Seq[String],
  tags: Tags,
  tiles: Seq[String] = Seq.empty
) extends LatLon
