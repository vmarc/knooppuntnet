package kpn.api.common.monitor

import kpn.api.common.BoundsI

case class MonitorRouteMapPage(
  routeId: Long,
  routeName: String,
  groupName: String,
  groupDescription: String,
  bounds: BoundsI,
  osmSegments: Seq[MonitorRouteSegment],
  okGeometry: Option[String],
  nokSegments: Seq[MonitorRouteNokSegment],
  reference: Option[MonitorRouteReferenceInfo]
)
