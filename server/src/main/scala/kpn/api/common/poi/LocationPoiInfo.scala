package kpn.api.common.poi

case class LocationPoiInfo(
  rowIndex: Long,
  _id: String,
  elementType: String,
  elementId: Long,
  layers: Seq[String],
  name: Option[String],
  subject: Option[String],
  description: Option[String],
  addressLine1: Option[String],
  addressLine2: Option[String],
  link: Boolean,
  image: Boolean
)
