package kpn.core.mongo.actions.base

import kpn.api.base.WithId
import kpn.core.mongo.Database
import kpn.core.mongo.actions.base.MongoDelete.log
import kpn.core.util.Log
import org.mongodb.scala.model.Filters.equal

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoDelete {
  private val log = Log(classOf[MongoDelete])
}

class MongoDelete(database: Database) {

  def execute[T](collectionName: String, _id: Long): Unit = {
    log.debugElapsed {
      val filter = equal("_id", _id)
      val collection = database.database.getCollection[WithId](collectionName)
      val future = collection.deleteOne(filter).toFuture()
      val result = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"collection: '$collectionName', _id: ${_id}", result)
    }
  }
}
