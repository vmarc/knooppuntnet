package kpn.core.mongo.actions.base

import kpn.core.mongo.actions.base.MongoQueryIds.log
import kpn.core.mongo.actions.base.MongoQueryIds.pipeline
import kpn.core.mongo.util.Id
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryIds extends MongoQuery {
  private val pipeline = readPipeline("pipeline")
  private val log = Log(classOf[MongoQueryIds])
}

class MongoQueryIds(database: MongoDatabase) {
  def execute(collectionName: String): Seq[Long] = {
    log.debugElapsed {
      val collection = database.getCollection(collectionName)
      val future = collection.aggregate[Id](pipeline.stages).toFuture()
      val docs = Await.result(future, Duration(2, TimeUnit.MINUTES))
      (s"collection: '$collectionName', ids: ${docs.size}", docs.map(_._id))
    }
  }
}
