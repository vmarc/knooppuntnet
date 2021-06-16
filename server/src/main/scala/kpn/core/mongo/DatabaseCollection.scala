package kpn.core.mongo

import kpn.api.base.WithId
import kpn.api.base.WithStringId
import kpn.core.mongo.DatabaseCollection.collectionLog
import kpn.core.mongo.actions.base.MongoQueryIds
import kpn.core.mongo.util.Id
import kpn.core.mongo.util.Mongo
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.ReplaceOptions

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.reflect.ClassTag

object DatabaseCollection {
  private val collectionLog = Log(classOf[DatabaseCollection[_]])
}

class DatabaseCollection[T: ClassTag](collection: MongoCollection[T]) {

  @deprecated
  def tempCollection: MongoCollection[T] = collection

  def aggregate[R: ClassTag](
    pipeline: Seq[Bson],
    log: Log = collectionLog,
    duration: Duration = Duration(30, TimeUnit.SECONDS)
  ): Seq[R] = {
    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }
    val future = collection.aggregate[R](pipeline).toFuture()
    Await.result(future, duration)
  }

  def stringPipelineAggregate[R: ClassTag](
    pipelineString: String,
    pipelineArgs: Map[String, String],
    log: Log = collectionLog,
    duration: Duration = Duration(30, TimeUnit.SECONDS)
  ): Seq[R] = {

    val pipelineStringWithArgs = pipelineArgs.foldLeft(pipelineString) { case (string, arg) =>
      string.replaceAll(arg._1, arg._2)
    }
    val pipeline = new MongoQuery().toPipeline(pipelineStringWithArgs)
    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }
    val future = collection.aggregate[R](pipeline).toFuture()
    Await.result(future, duration)
  }

  def find[R: ClassTag](filter: Bson): Option[R] = {
    val future = collection.find[R](filter).headOption()
    Await.result(future, Duration(30, TimeUnit.SECONDS))
  }

  def findById(_id: Long, log: Log = collectionLog): Option[T] = {
    log.debugElapsed {
      val future = collection.find[T](equal("_id", _id)).headOption()
      val doc = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"collection: '${collection.namespace.getCollectionName}', _id: ${_id}", doc)
    }
  }

  def save(doc: T, log: Log = collectionLog): Unit = {
    log.debugElapsed {
      val (id, filter) = doc match {
        case withId: WithId => (withId._id.toString, equal("_id", withId._id))
        case withStringId: WithStringId => (withStringId._id, equal("_id", withStringId._id))
        case _ => throw new IllegalArgumentException("document does not have een id")
      }
      val future = collection.replaceOne(filter, doc, ReplaceOptions().upsert(true)).toFuture()
      val result = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"collection: '$collectionName', _id: $id", result)
    }
  }

  def ids(log: Log = collectionLog): Seq[Long] = {
    log.debugElapsed {
      val future = collection.aggregate[Id](MongoQueryIds.pipeline.stages).toFuture()
      val docs = Await.result(future, Duration(2, TimeUnit.MINUTES))
      (s"collection: '$collectionName', ids: ${docs.size}", docs.map(_._id))
    }
  }

  private def collectionName: String = collection.namespace.getCollectionName
}
