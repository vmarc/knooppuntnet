package kpn.server.api.monitor.domain

import kpn.api.base.WithStringId
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.monitor.MonitorRouteNokSegment
import kpn.api.common.monitor.MonitorRouteSegment

case class MonitorRouteChangeGeometry(
  _id: String,
  key: ChangeKey,
  routeSegments: Seq[MonitorRouteSegment],
  newNokSegments: Seq[MonitorRouteNokSegment],
  resolvedNokSegments: Seq[MonitorRouteNokSegment],
) extends WithStringId
