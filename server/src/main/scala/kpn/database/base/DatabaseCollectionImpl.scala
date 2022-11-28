package kpn.database.base

import kpn.api.base.ObjectId
import kpn.api.base.WithId
import kpn.api.base.WithObjectId
import kpn.api.base.WithStringId
import kpn.core.util.Log
import kpn.database.util.Mongo
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.ReplaceOneModel
import org.mongodb.scala.model.ReplaceOptions

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.Awaitable
import scala.concurrent.duration.Duration
import scala.reflect.ClassTag

class DatabaseCollectionImpl[T: ClassTag](collection: MongoCollection[T]) extends DatabaseCollection[T] {

  override def native: MongoCollection[T] = collection

  override def aggregate[R: ClassTag](
    pipeline: Seq[Bson],
    log: Log,
    allowDiskUse: Boolean,
    duration: Duration
  ): Seq[R] = {
    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }

    val future = collection.aggregate[R](pipeline).allowDiskUse(allowDiskUse).toFuture()
    awaitAggregateResult(future, duration, pipeline, log)
  }

  override def optionAggregate[R: ClassTag](
    pipeline: Seq[Bson],
    log: Log,
    duration: Duration
  ): Option[R] = {
    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }
    val future = collection.aggregate[R](pipeline).headOption()
    awaitAggregateResult(future, duration, pipeline, log)
  }

  override def stringPipelineAggregate[R: ClassTag](
    pipelineString: String,
    pipelineArgs: Map[String, String],
    log: Log,
    duration: Duration
  ): Seq[R] = {

    val pipelineStringWithArgs = pipelineArgs.foldLeft(pipelineString) { case (string, arg) =>
      string.replaceAll(arg._1, arg._2)
    }
    val pipeline = new MongoQuery().toPipeline(pipelineStringWithArgs)
    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }
    val future = collection.aggregate[R](pipeline).toFuture()
    awaitAggregateResult(future, duration, pipeline, log)
  }

  override def findOne[R: ClassTag](filter: Bson, log: Log): Option[R] = {
    log.debugElapsed {
      val future = collection.find[R](filter).headOption()
      val doc = awaitResult(future, Duration(120, TimeUnit.SECONDS), log)
      (s"find - collection: '$collectionName'", doc)
    }
  }

  override def find[R: ClassTag](filter: Bson, log: Log): Seq[R] = {
    log.debugElapsed {
      val future = collection.find[R](filter).toFuture()
      val docs = awaitResult(future, Duration(5, TimeUnit.MINUTES), log)
      (s"find - collection: '$collectionName', docs= ${docs.size}", docs)
    }
  }

  override def findById(_id: Long, log: Log): Option[T] = {
    log.debugElapsed {
      val future = collection.find[T](equal("_id", _id)).headOption()
      val doc = awaitResult(future, Duration(30, TimeUnit.SECONDS), log)
      (s"findById - collection: '$collectionName', _id: ${_id}", doc)
    }
  }

  override def findByStringId(_id: String, log: Log): Option[T] = {
    log.debugElapsed {
      val future = collection.find[T](equal("_id", _id)).headOption()
      val doc = awaitResult(future, Duration(30, TimeUnit.SECONDS), log)
      (s"findById - collection: '$collectionName', _id: ${_id}", doc)
    }
  }

  override def findByObjectId(objectId: ObjectId, log: Log): Option[T] = {
    log.debugElapsed {
      val future = collection.find[T](equal("_id", objectId.raw)).headOption()
      val doc = awaitResult(future, Duration(30, TimeUnit.SECONDS), log)
      (s"findByObjectId - collection: '$collectionName', _id: $objectId", doc)
    }
  }

  override def findByIds(ids: Seq[Long], log: Log): Seq[T] = {
    if (ids.nonEmpty) {
      log.debugElapsed {
        val filter = in("_id", ids: _*)
        val future = collection.find[T](filter).toFuture()
        val docs = awaitResult(future, Duration(30, TimeUnit.SECONDS), log)
        (s"findByIds - collection: '$collectionName', ids: ${ids.mkString(", ")}", docs)
      }
    }
    else {
      Seq.empty
    }
  }

  override def findAll(log: Log): Seq[T] = {
    log.debugElapsed {
      val future = collection.find[T]().toFuture()
      val docs = awaitResult(future, Duration(30, TimeUnit.SECONDS), log)
      (s"find - collection: '$collectionName', docs: ${docs.size}", docs)
    }
  }

  override def save(doc: T, log: Log): Unit = {
    log.debugElapsed {

      val (id, filter) = doc match {
        case withId: WithId => (withId._id.toString, equal("_id", withId._id))
        case withStringId: WithStringId => (withStringId._id, equal("_id", withStringId._id))
        case withObjectId: WithObjectId => (withObjectId._id.oid, equal("_id", withObjectId._id.raw))
        case _ => throw new IllegalArgumentException("document does not have een id")
      }
      val future = collection.replaceOne(filter, doc, ReplaceOptions().upsert(true)).toFuture()
      val result = awaitResult(future, Duration(5, TimeUnit.MINUTES), log)
      (s"save - collection: '$collectionName', _id: $id", result)
    }
  }

  override def bulkSave(docs: Seq[T], log: Log): Unit = {
    if (docs.nonEmpty) {
      val requests = docs.map { doc =>
        val filter = doc match {
          case withId: WithId => equal("_id", withId._id)
          case withStringId: WithStringId => equal("_id", withStringId._id)
          case _ => throw new IllegalArgumentException("document does not have een id")
        }
        ReplaceOneModel[T](filter, doc, ReplaceOptions().upsert(true))
      }
      val future = native.bulkWrite(requests).toFuture()
      awaitResult(future, Duration(2, TimeUnit.MINUTES), log)
    }
  }

  override def delete(_id: Long, log: Log): Unit = {
    log.debugElapsed {
      val filter = equal("_id", _id)
      val future = collection.deleteOne(filter).toFuture()
      val result = awaitResult(future, Duration(30, TimeUnit.SECONDS), log)
      (s"delete - collection: '$collectionName', _id: ${_id}", result)
    }
  }

  override def deleteByStringId(_id: String, log: Log): Unit = {
    log.debugElapsed {
      val filter = equal("_id", _id)
      val future = collection.deleteOne(filter).toFuture()
      val result = awaitResult(future, Duration(30, TimeUnit.SECONDS), log)
      (s"delete - collection: '$collectionName', _id: ${_id}", result)
    }
  }

  override def deleteByObjectId(objectId: ObjectId, log: Log): Unit = {
    log.debugElapsed {
      val filter = equal("_id", objectId.raw)
      val future = collection.deleteOne(filter).toFuture()
      val result = awaitResult(future, Duration(30, TimeUnit.SECONDS), log)
      (s"delete - collection: '$collectionName', _id: ${objectId.oid}", result)
    }
  }

  override def deleteMany(filter: Bson, log: Log): Unit = {
    log.debugElapsed {
      val future = collection.deleteMany(filter).toFuture()
      val result = awaitResult(future, Duration(30, TimeUnit.SECONDS), log)
      (s"deleteMany - collection: '$collectionName'", result)
    }
  }

  override def ids(log: Log): Seq[Long] = {
    log.debugElapsed {
      val future = collection.find[Id]().projection(fields(include("_id"))).toFuture()
      val docs = awaitResult(future, Duration(15, TimeUnit.MINUTES), log)
      (s"collection: '$collectionName', ids: ${docs.size}", docs.map(_._id))
    }
  }

  override def stringIds(log: Log): Seq[String] = {
    log.debugElapsed {
      val future = collection.find[StringId]().projection(fields(include("_id"))).toFuture()
      val docs = awaitResult(future, Duration(15, TimeUnit.MINUTES), log)
      (s"collection: '$collectionName', ids: ${docs.size}", docs.map(_._id))
    }
  }

  override def objectIds(log: Log): Seq[ObjectId] = {
    aggregate[ObjectIdId](Seq(project(fields(include("_id"))))).map(_._id)
  }

  override def insertMany(docs: Seq[T], log: Log): Unit = {
    if (docs.nonEmpty) {
      log.debugElapsed {
        val ids = docs.map {
          case withId: WithId => withId._id
          case withStringId: WithStringId => withStringId._id
          case _ => "?"
        }
        val future = collection.insertMany(docs).toFuture()
        val result = awaitResult(future, Duration(1, TimeUnit.MINUTES), log)
        val resultString = if (!result.wasAcknowledged()) {
          ", not acknowledged"
        }
        else if (docs.size != result.getInsertedIds.size()) {
          s", docs.size does not match number of inserted docs: ${result.getInsertedIds.size()}"
        }
        else {
          ""
        }
        val message = s"insertMany - collection: '$collectionName', inserted ${docs.size} docs, ids: ${ids.mkString(", ")}$resultString"
        (message, ())
      }
    }
  }

  override def countDocuments(log: Log): Long = {
    log.debugElapsed {
      val future = collection.countDocuments().toFuture()
      val count = awaitResult(future, Duration(1, TimeUnit.MINUTES), log)
      val message = s"countDocuments - collection: '$collectionName' : $count"
      (message, count)
    }
  }

  override def countDocuments(filter: Bson, log: Log): Long = {
    log.debugElapsed {
      val future = collection.countDocuments(filter).toFuture()
      val count = awaitResult(future, Duration(1, TimeUnit.MINUTES), log)
      val message = s"countDocuments - collection: '$collectionName' : $count"
      (message, count)
    }
  }

  override def updateOne(filter: Bson, update: Seq[Bson], log: Log): Unit = {
    val future = collection.updateOne(filter, update).toFuture()
    val updateResult = awaitResult(future, Duration(1, TimeUnit.MINUTES), log)
    val message = s"update - collection: '$collectionName' : ${updateResult.getModifiedCount}"
    (message, ())
  }

  override def drop(log: Log): Unit = {
    val future = collection.drop().toFuture()
    awaitResult(future, Duration(1, TimeUnit.MINUTES), log)
  }

  private def collectionName: String = collection.namespace.getCollectionName

  private def awaitAggregateResult[A](awaitable: Awaitable[A], duration: Duration, pipeline: Seq[Bson], log: Log): A = {
    try {
      Await.result(awaitable, duration)
    }
    catch {
      case e: Exception =>
        val pipelineString = Mongo.pipelineString(pipeline)
        val message = s"Error executing aggregation pipeline on collection '$collectionName'\n$pipelineString"
        val wrapperException = new RuntimeException(message, e)
        log.error("mongdb error", wrapperException)
        throw wrapperException
    }
  }

  private def awaitResult[A](awaitable: Awaitable[A], duration: Duration, log: Log): A = {
    try {
      Await.result(awaitable, duration)
    }
    catch {
      case e: Exception =>
        val message = s"Error in collection '$collectionName'"
        val wrapperException = new RuntimeException(message, e)
        log.error("mongdb error", wrapperException)
        throw wrapperException
    }
  }
}
