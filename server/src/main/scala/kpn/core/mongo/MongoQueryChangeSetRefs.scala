package kpn.core.mongo

import kpn.core.mongo.MongoQueryChangeSetRefs.pipeline
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryChangeSetRefs extends MongoQuery {
  private val pipeline = readPipeline("pipeline")
}

class MongoQueryChangeSetRefs(database: MongoDatabase) {

  private val log = Log(classOf[MongoQueryChangeSetRefs])

  def execute(): Seq[ChangeSetRef] = {
    log.debugElapsed {
      val collection = database.getCollection("change-summaries")
      val future = collection.aggregate[ChangeSetRef](pipeline.stages).toFuture()
      val values = Await.result(future, Duration(1, TimeUnit.MINUTES))
      (s"${values.size} changeSetRefs", values)
    }
  }
}
