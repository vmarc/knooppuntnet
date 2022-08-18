package kpn.api.common.monitor

import kpn.api.base.ObjectId
import kpn.api.common.Bounds

case class MonitorRouteMapPage(
  routeId: ObjectId,
  relationId: Option[Long],
  routeName: String,
  groupName: String,
  groupDescription: String,
  bounds: Bounds,
  osmSegments: Seq[MonitorRouteSegment],
  okGeometry: Option[String],
  nokSegments: Seq[MonitorRouteNokSegment],
  reference: Option[MonitorRouteReferenceInfo]
)
