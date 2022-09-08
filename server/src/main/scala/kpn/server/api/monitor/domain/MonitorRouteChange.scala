package kpn.server.api.monitor.domain

import kpn.api.base.ObjectId
import kpn.api.base.WithObjectId
import kpn.api.common.changes.details.ChangeKey

object MonitorRouteChange {

  def apply(
    key: ChangeKey,
    wayCount: Long,
    waysAdded: Long,
    waysRemoved: Long,
    waysUpdated: Long,
    osmDistance: Long,
    routeSegmentCount: Long,
    newDeviationCount: Long,
    resolvedDeviationCount: Long,
    happy: Boolean,
    investigate: Boolean
  ): MonitorRouteChange = {
    MonitorRouteChange(
      ObjectId(),
      ObjectId("TODO"), // key.toId,
      key,
      wayCount,
      waysAdded,
      waysRemoved,
      waysUpdated,
      osmDistance,
      routeSegmentCount,
      newDeviationCount,
      resolvedDeviationCount,
      happy,
      investigate
    )
  }
}

case class MonitorRouteChange(
  _id: ObjectId,
  routeId: ObjectId,
  key: ChangeKey,
  wayCount: Long,
  waysAdded: Long,
  waysRemoved: Long,
  waysUpdated: Long,
  osmDistance: Long,
  routeSegmentCount: Long,
  newDeviationCount: Long,
  resolvedDeviationCount: Long,
  happy: Boolean,
  investigate: Boolean
) extends WithObjectId
