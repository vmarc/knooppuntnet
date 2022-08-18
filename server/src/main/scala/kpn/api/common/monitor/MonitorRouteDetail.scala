package kpn.api.common.monitor

import kpn.api.base.ObjectId

case class MonitorRouteDetail(
  index: Long,
  routeId: ObjectId,
  name: String,
  description: String,
  relationId: Option[Long],
  wayCount: Long,
  osmDistance: Long,
  gpxDistance: Long,
  gpxFilename: Option[String],
  osmHappy: Boolean,
  gpxHappy: Boolean,
  happy: Boolean,
)
