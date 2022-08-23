package kpn.api.common.monitor

case class MonitorRouteProperties(
  name: String,
  description: String,
  relationId: Option[String],
  referenceType: String,
  referenceTimestamp: Option[String], // TODO MON or Timestamp?
  gpxFilename: Option[String]
)
