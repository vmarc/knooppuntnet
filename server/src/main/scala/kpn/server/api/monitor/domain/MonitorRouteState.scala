package kpn.server.api.monitor.domain

import kpn.api.base.WithId
import kpn.api.common.BoundsI
import kpn.api.common.monitor.MonitorRouteNokSegment
import kpn.api.common.monitor.MonitorRouteSegment
import kpn.api.custom.Timestamp

object MonitorRouteState {

  // for mongodb migration only
  def apply(
    _id: Long,
    timestamp: Timestamp,
    wayCount: Long,
    osmDistance: Long,
    gpxDistance: Long,
    bounds: BoundsI,
    referenceKey: Option[String],
    osmSegments: Seq[MonitorRouteSegment],
    okGeometry: Option[String],
    nokSegments: Seq[MonitorRouteNokSegment]
  ): MonitorRouteState = {
    MonitorRouteState(
      _id,
      _id,
      timestamp,
      wayCount,
      osmDistance,
      gpxDistance,
      bounds,
      referenceKey,
      osmSegments,
      okGeometry,
      nokSegments
    )
  }
}

case class MonitorRouteState(
  _id: Long, // routeId
  routeId: Long,
  timestamp: Timestamp, // time of most recent analysis
  wayCount: Long,
  osmDistance: Long,
  gpxDistance: Long,
  bounds: BoundsI,
  referenceKey: Option[String], // use this to pick up the reference geometry
  osmSegments: Seq[MonitorRouteSegment],
  okGeometry: Option[String],
  nokSegments: Seq[MonitorRouteNokSegment]
) extends WithId {

  // for mongodb migration only
  def toMongo: MonitorRouteState = {
    copy(_id = routeId)
  }
}
