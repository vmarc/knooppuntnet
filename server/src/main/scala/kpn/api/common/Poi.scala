package kpn.api.common

import kpn.api.base.WithStringId
import kpn.api.common.location.Location
import kpn.api.custom.Tags

case class Poi(
  _id: String,
  elementType: String,
  elementId: Long,
  latitude: String,
  longitude: String,
  layers: Seq[String],
  tags: Tags,
  location: Location,
  tiles: Seq[String]
) extends LatLon with WithStringId
