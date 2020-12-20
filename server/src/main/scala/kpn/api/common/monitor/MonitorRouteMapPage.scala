package kpn.api.common.monitor

import kpn.api.common.BoundsI

case class MonitorRouteMapPage(
  id: Long,
  ref: Option[String],
  name: String,
  bounds: BoundsI,
  osmSegments: Seq[MonitorRouteSegment],
  okGeometry: Option[String],
  nokSegments: Seq[MonitorRouteNokSegment],
  reference: Option[MonitorRouteReferenceInfo]
)
