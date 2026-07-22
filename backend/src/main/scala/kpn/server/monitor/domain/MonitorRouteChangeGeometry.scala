package kpn.server.monitor.domain

import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.api.common.monitor.MonitorRouteSegment
import kpn.core.doc.WithObjectId
import org.bson.types.ObjectId

case class MonitorRouteChangeGeometry(
  _id: ObjectId,
  routeId: ObjectId,
  key: ChangeKey,
  routeSegments: Seq[MonitorRouteSegment],
  newDeviations: Seq[MonitorRouteDeviation],
  resolvedDeviations: Seq[MonitorRouteDeviation],
) extends WithObjectId
