package kpn.server.api.monitor.domain

import kpn.api.base.ObjectId
import kpn.api.base.WithObjectId
import kpn.api.base.WithStringId
import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorRouteNokSegment
import kpn.api.common.monitor.MonitorRouteSegment
import kpn.api.custom.Timestamp

case class MonitorRouteState(
  _id: ObjectId,
  routeId: ObjectId,
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
) extends WithObjectId
