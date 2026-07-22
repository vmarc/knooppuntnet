package kpn.server.monitor.domain

import kpn.api.common.changes.details.ChangeKey
import kpn.core.doc.WithObjectId
import org.bson.types.ObjectId

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
      ObjectId.get(),
      new ObjectId("TODO"),
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
