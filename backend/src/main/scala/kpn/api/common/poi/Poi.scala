package kpn.api.common.poi

import kpn.api.common.LatLon
import kpn.api.common.data.Tagable
import kpn.api.common.location.Location
import kpn.api.custom.Tag
import kpn.core.doc.WithStringId

case class Poi(
  _id: String,
  elementType: String,
  elementId: Long,
  latitude: String,
  longitude: String,
  layers: Seq[String],
  tags: Seq[Tag],
  location: Location,
  tiles: Seq[String],
  description: Option[String],
  address: Option[String],
  link: Boolean,
  image: Boolean
) extends LatLon with WithStringId with Tagable
