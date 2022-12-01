package kpn.api.common.monitor

import kpn.api.custom.Day

case class MonitorRouteDetailsPage(
  routeId: String,
  groupName: String,
  groupDescription: String,
  routeName: String,
  routeDescription: String,
  relationId: Option[Long],
  comment: Option[String],
  referenceType: Option[String],
  referenceDay: Option[Day],
  referenceFilename: Option[String],
  referenceDistance: Long,
  deviationDistance: Long,
  deviationCount: Long,
  osmSegmentCount: Long,
  happy: Boolean,
  wayCount: Long,
  osmDistance: Long,
)
