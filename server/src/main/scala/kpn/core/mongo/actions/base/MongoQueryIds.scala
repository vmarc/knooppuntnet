package kpn.core.mongo.actions.base

import kpn.core.mongo.Database
import kpn.core.mongo.actions.base.MongoQueryIds.log
import kpn.core.mongo.actions.base.MongoQueryIds.pipeline
import kpn.core.mongo.util.Id
import kpn.core.mongo.util.MongoQuery
import kpn.core.mongo.util.Pipeline
import kpn.core.util.Log

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryIds extends MongoQuery {
  val pipeline: Pipeline = readPipeline("pipeline")
  private val log = Log(classOf[MongoQueryIds])
}

class MongoQueryIds(database: Database) {
  def execute(collectionName: String): Seq[Long] = {
    log.debugElapsed {
      val collection = database.database.getCollection(collectionName)
      val future = collection.aggregate[Id](pipeline.stages).toFuture()
      val docs = Await.result(future, Duration(2, TimeUnit.MINUTES))
      (s"collection: '$collectionName', ids: ${docs.size}", docs.map(_._id))
    }
  }
}
