package kpn.api.common.monitor

import kpn.api.base.MongoId

case class MonitorRouteDetail(
  index: Long,
  routeId: MongoId,
  name: String,
  description: String,
  relationId: Long,
  wayCount: Long,
  osmDistance: Long,
  gpxDistance: Long,
  gpxFilename: Option[String],
  osmHappy: Boolean,
  gpxHappy: Boolean,
  happy: Boolean,
)
