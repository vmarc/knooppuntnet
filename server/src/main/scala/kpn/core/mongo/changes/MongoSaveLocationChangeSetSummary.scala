package kpn.core.mongo.changes

import kpn.api.common.LocationChangeSetSummary
import kpn.core.database.doc.LocationChangeSetSummaryDoc
import kpn.core.mongo.changes.MongoSaveLocationChangeSetSummary.log
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
      val id = s"change:${summary.key.changeSetId}:${summary.key.replicationNumber}:location-summary:0"
      val doc = LocationChangeSetSummaryDoc(id, summary)
      val filter = equal("_id", id)
      val collection = database.getCollection[LocationChangeSetSummaryDoc]("change-location-summaries")
      val future = collection.replaceOne(filter, doc, ReplaceOptions().upsert(true)).toFuture()
      val result = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"save $id", result)
    }
  }
}
