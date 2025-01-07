package kpn.server.sync

import kpn.api.base.ObjectId
import kpn.api.base.WithObjectId
import kpn.api.custom.Timestamp
import kpn.core.common.Time

object Transaction {
  def routeUpdate(routeId: Long): Transaction = {
    Transaction(ObjectId(), Time.now, "routes", "update", routeId)
  }

  def routeDelete(routeId: Long): Transaction = {
    Transaction(ObjectId(), Time.now, "routes", "delete", routeId)
  }
}

case class Transaction(
  _id: ObjectId,
  timestamp: Timestamp,
  collection: String,
  action: String,
  id: Long
) extends WithObjectId
