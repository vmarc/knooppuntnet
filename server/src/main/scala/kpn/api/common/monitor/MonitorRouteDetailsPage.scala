package kpn.api.common.monitor

import kpn.api.base.MongoId

case class MonitorRouteDetailsPage(
  routeId: MongoId,
  groupName: String,
  groupDescription: String,
  routeName: String,
  routeDescription: String,
  relationId: Option[Long],
  wayCount: Long,
  osmDistance: Long,
  gpxDistance: Long,
  gpxFilename: Option[String],
  happy: Boolean,
  osmSegmentCount: Long,
  gpxNokSegmentCount: Long
)
