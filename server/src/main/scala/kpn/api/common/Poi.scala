package kpn.api.common

import kpn.api.base.WithStringId
import kpn.api.custom.Tags

case class Poi(
  _id: String,
  elementType: String,
  elementId: Long,
  latitude: String,
  longitude: String,
  layers: Seq[String],
  tags: Tags,
  tiles: Seq[String] = Seq.empty
) extends LatLon with WithStringId
