package kpn.api.common.monitor

import kpn.api.custom.Timestamp

case class MonitorRouteGpxPage(
  groupName: String,
  routeName: String,
  subRelationId: Long,
  subRelationDescription: String,
  referenceTimestamp: Timestamp,
  referenceFilename: Option[String],
  referenceDistance: Long
)
