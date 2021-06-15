package kpn.core.mongo.changes

import kpn.api.base.WithId
import kpn.core.mongo.changes.MongoSave.log
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.ReplaceOptions

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoSave {
  private val log = Log(classOf[MongoSave])
}

class MongoSave(database: MongoDatabase) {

  def execute[T](collectionName: String, withId: WithId): Unit = {
    log.debugElapsed {
      val filter = equal("_id", withId._id)
      val collection = database.getCollection[WithId](collectionName)
      val future = collection.replaceOne(filter, withId, ReplaceOptions().upsert(true)).toFuture()
      val result = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"collection: '$collectionName', _id: ${withId._id}", result)
    }
  }
}
