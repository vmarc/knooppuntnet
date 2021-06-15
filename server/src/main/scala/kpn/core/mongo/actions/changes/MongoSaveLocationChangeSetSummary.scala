package kpn.core.mongo.actions.changes

import kpn.api.common.LocationChangeSetSummary
import kpn.core.mongo.actions.changes.MongoSaveLocationChangeSetSummary.log
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.ReplaceOptions

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoSaveLocationChangeSetSummary {
  private val log = Log(classOf[MongoSaveLocationChangeSetSummary])
}

class MongoSaveLocationChangeSetSummary(database: MongoDatabase) {

  def execute(summary: LocationChangeSetSummary): Unit = {
    log.debugElapsed {
      val filter = equal("_id", summary._id)
      val collection = database.getCollection[LocationChangeSetSummary]("change-location-summaries")
      val future = collection.replaceOne(filter, summary, ReplaceOptions().upsert(true)).toFuture()
      val result = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"save ${summary._id}", result)
    }
  }
}
