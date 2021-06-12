package kpn.core.mongo.changes

import kpn.api.common.ChangeSetSummary
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
      val filter = equal("_id", changeSetSummary._id)
      val collection = database.getCollection[ChangeSetSummary]("changeset-summaries")
      val future = collection.replaceOne(filter, changeSetSummary, ReplaceOptions().upsert(true)).toFuture()
      val result = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"save ${changeSetSummary._id}", result)
    }
  }
}
