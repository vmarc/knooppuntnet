package kpn.server.api.monitor.domain

import kpn.api.base.WithStringId
import kpn.api.common.BoundsI
import kpn.api.custom.Timestamp

object MonitorRouteReference {

  def apply(
    routeId: Long,
    key: String,
    created: Timestamp,
    user: String,
    bounds: BoundsI,
    referenceType: String,
    referenceTimestamp: Option[Timestamp],
    segmentCount: Long,
    filename: Option[String],
    geometry: String
  ): MonitorRouteReference = {
    MonitorRouteReference(
      id(routeId, key),
      routeId,
      key,
      created,
      user,
      bounds,
      referenceType,
      referenceTimestamp,
      segmentCount,
      filename,
      geometry
    )
  }

  def id(routeId: Long, key: String): String = {
    s"$routeId:$key"
  }
}

case class MonitorRouteReference(
  _id: String,
  routeId: Long,
  key: String, // YYYYMMDDHHMMSS derived from created Timestamp
  created: Timestamp,
  user: String,
  bounds: BoundsI,
  referenceType: String, // "osm" | "gpx"
  referenceTimestamp: Option[Timestamp],
  segmentCount: Long,
  filename: Option[String],
  geometry: String // osm | gpx
) extends WithStringId {

  // for mongodb migration only
  def toMongo: MonitorRouteReference = {
    copy(_id = MonitorRouteReference.id(routeId, key))
  }
}
