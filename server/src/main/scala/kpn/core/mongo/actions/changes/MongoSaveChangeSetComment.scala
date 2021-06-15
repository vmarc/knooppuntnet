package kpn.core.mongo.actions.changes

import kpn.core.mongo.actions.changes.MongoSaveChangeSetComment.log
import kpn.core.mongo.migration.ChangeSetComment
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.ReplaceOptions

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoSaveChangeSetComment {
  private val log = Log(classOf[MongoSaveChangeSetComment])
}

class MongoSaveChangeSetComment(database: MongoDatabase) {

  def execute(changeSetComment: ChangeSetComment): Unit = {
    log.debugElapsed {
      val filter = equal("_id", changeSetComment._id)
      val collection = database.getCollection[ChangeSetComment]("changeset-comments")
      val future = collection.replaceOne(filter, changeSetComment, ReplaceOptions().upsert(true)).toFuture()
      val result = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"save changeSetComment ${changeSetComment._id}", result)
    }
  }
}
