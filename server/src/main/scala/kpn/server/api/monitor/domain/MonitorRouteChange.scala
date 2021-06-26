package kpn.server.api.monitor.domain

import kpn.api.base.WithStringId
import kpn.api.common.changes.details.ChangeKey

object MonitorRouteChange {

  def apply(
    key: ChangeKey,
    groupName: String,
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
      key.toId,
      key,
      groupName,
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
  _id: String,
  key: ChangeKey,
  groupName: String,
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
) extends WithStringId {

  // for mongodb migration only
  def toMongo: MonitorRouteChange = {
    copy(_id = key.toId)
  }
}
