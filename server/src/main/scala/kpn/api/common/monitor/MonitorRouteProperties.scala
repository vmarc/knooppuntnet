package kpn.api.common.monitor

import kpn.api.custom.Day

case class MonitorRouteProperties(
  groupName: String,
  name: String,
  description: String,
  comment: Option[String],
  relationId: Option[Long],
  referenceType: String, // "osm" | "gpx" | "multi-gpx"
  referenceDay: Option[Day],
  referenceFilename: Option[String],
  referenceFileChanged: Boolean,
)
