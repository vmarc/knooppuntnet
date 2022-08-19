package kpn.server.api.monitor.domain

import kpn.api.base.ObjectId
import kpn.api.base.WithObjectId
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.monitor.MonitorRouteNokSegment
import kpn.api.common.monitor.MonitorRouteSegment

case class MonitorRouteChangeGeometry(
  _id: ObjectId,
  routeId: ObjectId,
  key: ChangeKey,
  routeSegments: Seq[MonitorRouteSegment],
  newNokSegments: Seq[MonitorRouteNokSegment],
  resolvedNokSegments: Seq[MonitorRouteNokSegment],
) extends WithObjectId
