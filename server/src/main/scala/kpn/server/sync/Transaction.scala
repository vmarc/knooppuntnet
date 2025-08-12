package kpn.server.sync

import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.doc.WithObjectId
import org.bson.types.ObjectId

object Transaction {
  def routeUpdate(routeId: Long): Transaction = {
    Transaction(ObjectId.get(), Time.now, "routes", "update", routeId)
  }

  def routeDelete(routeId: Long): Transaction = {
    Transaction(ObjectId.get(), Time.now, "routes", "delete", routeId)
  }
}

case class Transaction(
  _id: ObjectId,
  timestamp: Timestamp,
  collection: String,
  action: String,
  id: Long
) extends WithObjectId
