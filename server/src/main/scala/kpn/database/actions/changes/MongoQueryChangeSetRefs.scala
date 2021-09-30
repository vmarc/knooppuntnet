package kpn.database.actions.changes

import kpn.database.actions.changes.MongoQueryChangeSetRefs.log
import kpn.database.actions.changes.MongoQueryChangeSetRefs.pipeline
import kpn.database.actions.statistics.ChangeSetRef
import kpn.database.base.Database
import kpn.database.base.MongoQuery
import kpn.core.util.Log

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryChangeSetRefs extends MongoQuery {
  private val log = Log(classOf[MongoQueryChangeSetRefs])
  private val pipeline = readPipeline("pipeline")
}

class MongoQueryChangeSetRefs(database: Database) {

  def execute(): Seq[ChangeSetRef] = {
    log.debugElapsed {
      val collection = database.getCollection("changeset-summaries")
      val future = collection.aggregate[ChangeSetRef](pipeline.stages).toFuture()
      val values = Await.result(future, Duration(1, TimeUnit.MINUTES))
      (s"${values.size} changeSetRefs", values)
    }
  }
}
