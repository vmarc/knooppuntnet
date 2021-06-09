package kpn.core.mongo.changes

import kpn.core.mongo.changes.MongoQueryChangeSetRefs.log
import kpn.core.mongo.changes.MongoQueryChangeSetRefs.pipeline
import kpn.core.mongo.statistics.ChangeSetRef
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryChangeSetRefs extends MongoQuery {
  private val log = Log(classOf[MongoQueryChangeSetRefs])
  private val pipeline = readPipeline("pipeline")
}

class MongoQueryChangeSetRefs(database: MongoDatabase) {

  def execute(): Seq[ChangeSetRef] = {
    log.debugElapsed {
      val collection = database.getCollection("changeset-summaries")
      val future = collection.aggregate[ChangeSetRef](pipeline.stages).toFuture()
      val values = Await.result(future, Duration(1, TimeUnit.MINUTES))
      (s"${values.size} changeSetRefs", values)
    }
  }
}
