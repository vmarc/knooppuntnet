package kpn.core.mongo.changes

import kpn.api.common.ChangeSetSummary
import kpn.core.database.doc.ChangeSetSummaryDoc
import kpn.core.mongo.changes.MongoSaveChangeSetSummary.log
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.ReplaceOptions

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoSaveChangeSetSummary {
  private val log = Log(classOf[MongoSaveChangeSetSummary])
}

class MongoSaveChangeSetSummary(database: MongoDatabase) {

  def execute(changeSetSummary: ChangeSetSummary): Unit = {
    log.debugElapsed {
      val id = s"change:${changeSetSummary.key.changeSetId}:${changeSetSummary.key.replicationNumber}:summary:0"
      val doc = ChangeSetSummaryDoc(id, changeSetSummary)
      val filter = equal("_id", id)
      val collection = database.getCollection[ChangeSetSummaryDoc]("changeset-summaries")
      val future = collection.replaceOne(filter, doc, ReplaceOptions().upsert(true)).toFuture()
      val result = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"save $id", result)
    }
  }
}
