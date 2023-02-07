package kpn.server.monitor.domain

import kpn.api.base.ObjectId
import kpn.api.base.WithObjectId
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.api.common.monitor.MonitorRouteSegment

case class MonitorRouteChangeGeometry(
  _id: ObjectId,
  routeId: ObjectId,
  key: ChangeKey,
  routeSegments: Seq[MonitorRouteSegment],
  newDeviations: Seq[MonitorRouteDeviation],
  resolvedDeviations: Seq[MonitorRouteDeviation],
) extends WithObjectId
