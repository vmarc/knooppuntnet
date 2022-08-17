package kpn.server.api.monitor.domain

import kpn.api.base.MongoId
import kpn.api.base.WithMongoId
import kpn.api.base.WithStringId
import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorRouteNokSegment
import kpn.api.common.monitor.MonitorRouteSegment
import kpn.api.custom.Timestamp

case class MonitorRouteState(
  _id: MongoId,
  routeId: MongoId,
  timestamp: Timestamp, // time of most recent analysis
  wayCount: Long,
  osmDistance: Long,
  gpxDistance: Long,
  bounds: Bounds,
  referenceKey: Option[String], // use this to pick up the reference geometry
  osmSegments: Seq[MonitorRouteSegment],
  okGeometry: Option[String],
  nokSegments: Seq[MonitorRouteNokSegment],
  happy: Boolean
) extends WithMongoId
