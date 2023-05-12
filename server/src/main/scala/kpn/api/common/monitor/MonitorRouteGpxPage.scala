package kpn.api.common.monitor

import kpn.api.custom.Day

case class MonitorRouteGpxPage(
  groupName: String,
  routeName: String,
  subRelationId: Long,
  subRelationDescription: String,
  referenceDay: Day,
  referenceFilename: Option[String],
)
