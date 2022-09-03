package kpn.api.common.monitor

import kpn.api.custom.Day

case class MonitorRouteProperties(
  name: String,
  description: String,
  groupName: String,
  relationId: Option[Long],
  referenceType: String,
  osmReferenceDay: Option[Day],
  gpxFileChanged: Boolean,
  gpxFilename: Option[String]
)
