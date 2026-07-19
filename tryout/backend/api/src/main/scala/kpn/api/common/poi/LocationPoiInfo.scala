package kpn.api.common.poi

case class LocationPoiInfo(
  rowIndex: Long,
  _id: String,
  elementType: String,
  elementId: Long,
  layers: Seq[String],
  description: Option[String],
  address: Option[String],
  link: Boolean,
  image: Boolean
)
