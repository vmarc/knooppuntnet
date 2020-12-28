package kpn.server.api.monitor.domain

import kpn.api.common.changes.details.ChangeKeyI
import kpn.api.common.monitor.MonitorRouteNokSegment
import kpn.api.common.monitor.MonitorRouteSegment

case class MonitorRouteChangeGeometry(
  key: ChangeKeyI,
  routeSegments: Seq[MonitorRouteSegment],
  newNokSegments: Seq[MonitorRouteNokSegment],
  resolvedNokSegments: Seq[MonitorRouteNokSegment],
)
