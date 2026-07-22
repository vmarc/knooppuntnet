package kpn.server.sync

import kpn.api.custom.Timestamp
import kpn.api.id.WithObjectId
import kpn.api.time.Time
import org.bson.types.ObjectId

object Transaction {
  def routeUpdate(routeId: Long): Transaction = {
    Transaction(ObjectId.get(), Time.now, "routes", "update", routeId)
  }

  def routeDelete(routeId: Long): Transaction = {
    Transaction(ObjectId.get(), Time.now, "routes", "delete", routeId)
  }

  def update(collection: String, objectId: Long): Transaction = {
    Transaction(
      ObjectId.get(),
      Time.now,
      collection,
      "update",
      objectId
    )
  }
}

case class Transaction(
  _id: ObjectId,
  timestamp: Timestamp,
  collection: String,
  action: String,
  id: Long
) extends WithObjectId
