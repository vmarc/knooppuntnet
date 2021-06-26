package kpn.server.api.monitor.domain

import kpn.api.base.WithStringId
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.monitor.MonitorRouteNokSegment
import kpn.api.common.monitor.MonitorRouteSegment

object MonitorRouteChangeGeometry {

  def apply(
    key: ChangeKey,
    routeSegments: Seq[MonitorRouteSegment],
    newNokSegments: Seq[MonitorRouteNokSegment],
    resolvedNokSegments: Seq[MonitorRouteNokSegment],
  ): MonitorRouteChangeGeometry = {
    MonitorRouteChangeGeometry(
      key.toId,
      key,
      routeSegments,
      newNokSegments,
      resolvedNokSegments
    )
  }
}

case class MonitorRouteChangeGeometry(
  _id: String,
  key: ChangeKey,
  routeSegments: Seq[MonitorRouteSegment],
  newNokSegments: Seq[MonitorRouteNokSegment],
  resolvedNokSegments: Seq[MonitorRouteNokSegment],
) extends WithStringId {

  // for mongodb migration only
  def toMongo: MonitorRouteChangeGeometry = {
    copy(_id = key.toId)
  }
}
