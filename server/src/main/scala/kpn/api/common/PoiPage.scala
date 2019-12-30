package kpn.api.common

import kpn.api.custom.Tags

case class PoiPage(
  elementType: String,
  elementId: Long,
  latitude: String,
  longitude: String,
  layers: Seq[String],
  mainTags: Tags,
  extraTags: Tags,
  name: Option[String],
  subject: Option[String],
  description: Option[String],
  addressLine1: Option[String],
  addressLine2: Option[String],
  phone: Option[String],
  email: Option[String],
  website: Option[String],
  image: Option[String],
  wheelchair: Option[String]
)
