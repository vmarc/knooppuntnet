package kpn.server.monitor.domain

import kpn.api.common.Bounds

case class MonitorRouteOsmSegmentElement(
  relationId: Long,
  segmentId: Long,
  meters: Long,
  bounds: Bounds,
  reversed: Boolean
)
