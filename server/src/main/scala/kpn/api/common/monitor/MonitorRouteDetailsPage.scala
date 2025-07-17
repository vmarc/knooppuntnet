package kpn.api.common.monitor

import kpn.api.common.Bounds
import kpn.api.common.route.RouteDetails
import kpn.api.custom.Timestamp

case class MonitorRouteDetailsPage(
  adminRole: Boolean,
  groupName: String,
  groupDescription: String,
  routeName: String,
  routeDescription: String,
  routeId: String,
  relationId: Option[Long],
  comment: Option[String],
  symbol: Option[String],
  analysisTimestamp: Option[Timestamp],
  analysisDuration: Option[Long],
  referenceType: MonitorReferenceType,
  referenceTimestamp: Option[Timestamp],
  referenceFilename: Option[String],
  referenceDistance: Long,
  deviationDistance: Long,
  deviationCount: Long,
  osmSegmentCount: Long,
  happy: Boolean,
  wayCount: Long,
  osmDistance: Long,
  relationCount: Long,
  relationLevels: Long,
  details: RouteDetails,
  bounds: Option[Bounds]
)
