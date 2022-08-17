package kpn.server.api.monitor.domain

import kpn.api.base.MongoId
import kpn.api.base.WithMongoId
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
      MongoId(),
      MongoId("TODO"), // key.toId,
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
  _id: MongoId,
  monitorRoutId: MongoId,
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
) extends WithMongoId
