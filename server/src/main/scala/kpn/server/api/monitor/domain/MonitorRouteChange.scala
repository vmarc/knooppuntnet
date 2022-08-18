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
    newNokSegmentCount: Long,
    resolvedNokSegmentCount: Long,
    referenceKey: String,
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
      newNokSegmentCount,
      resolvedNokSegmentCount,
      referenceKey,
      happy,
      investigate
    )
  }
}

case class MonitorRouteChange(
  _id: ObjectId,
  monitorRoutId: ObjectId,
  key: ChangeKey,
  wayCount: Long,
  waysAdded: Long,
  waysRemoved: Long,
  waysUpdated: Long,
  osmDistance: Long,
  routeSegmentCount: Long,
  newNokSegmentCount: Long,
  resolvedNokSegmentCount: Long,
  referenceKey: String,
  happy: Boolean,
  investigate: Boolean
) extends WithObjectId
