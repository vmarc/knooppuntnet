package kpn.core.mongo

import kpn.api.base.WithId
import kpn.api.base.WithStringId
import kpn.core.mongo.util.Id
import kpn.core.mongo.util.Mongo
import kpn.core.mongo.util.MongoQuery
import kpn.core.mongo.util.StringId
import kpn.core.util.Log
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.ReplaceOptions

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.reflect.ClassTag

class DatabaseCollectionImpl[T: ClassTag](collection: MongoCollection[T]) extends DatabaseCollection[T] {

  override def native: MongoCollection[T] = collection

  override def aggregate[R: ClassTag](
    pipeline: Seq[Bson],
    log: Log,
    duration: Duration
  ): Seq[R] = {
    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }
    val future = collection.aggregate[R](pipeline).toFuture()
    Await.result(future, duration)
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
    Await.result(future, duration)
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
    Await.result(future, duration)
  }

  override def findOne[R: ClassTag](filter: Bson, log: Log): Option[R] = {
    log.debugElapsed {
      val future = collection.find[R](filter).headOption()
      val doc = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"find - collection: '${collection.namespace.getCollectionName}'", doc)
    }
  }

  override def find[R: ClassTag](filter: Bson, log: Log): Seq[R] = {
    log.debugElapsed {
      val future = collection.find[R](filter).toFuture()
      val docs = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"find - collection: '${collection.namespace.getCollectionName}', docs= ${docs.size}", docs)
    }
  }

  override def findById(_id: Long, log: Log): Option[T] = {
    log.debugElapsed {
      val future = collection.find[T](equal("_id", _id)).headOption()
      val doc = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"findById - collection: '${collection.namespace.getCollectionName}', _id: ${_id}", doc)
    }
  }

  override def findByStringId(_id: String, log: Log): Option[T] = {
    log.debugElapsed {
      val future = collection.find[T](equal("_id", _id)).headOption()
      val doc = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"findById - collection: '${collection.namespace.getCollectionName}', _id: ${_id}", doc)
    }
  }

  override def findByIds(ids: Seq[Long], log: Log): Seq[T] = {
    log.debugElapsed {
      val filter = in("_id", ids: _*)
      val future = collection.find[T](filter).toFuture()
      val docs = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"findByIds - collection: '${collection.namespace.getCollectionName}', ids: ${ids.mkString(", ")}", docs)
    }
  }

  override def findAll(log: Log): Seq[T] = {
    log.debugElapsed {
      val future = collection.find[T]().toFuture()
      val docs = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"find - collection: '${collection.namespace.getCollectionName}', docs: ${docs.size}", docs)
    }
  }

  override def save(doc: T, log: Log): Unit = {
    log.debugElapsed {
      val (id, filter) = doc match {
        case withId: WithId => (withId._id.toString, equal("_id", withId._id))
        case withStringId: WithStringId => (withStringId._id, equal("_id", withStringId._id))
        case _ => throw new IllegalArgumentException("document does not have een id")
      }
      val future = collection.replaceOne(filter, doc, ReplaceOptions().upsert(true)).toFuture()
      val result = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"save - collection: '$collectionName', _id: $id", result)
    }
  }

  override def delete(_id: Long, log: Log): Unit = {
    log.debugElapsed {
      val filter = equal("_id", _id)
      val future = collection.deleteOne(filter).toFuture()
      val result = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"delete - collection: '$collectionName', _id: ${_id}", result)
    }
  }

  override def deleteByStringId(_id: String, log: Log): Unit = {
    log.debugElapsed {
      val filter = equal("_id", _id)
      val future = collection.deleteOne(filter).toFuture()
      val result = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"delete - collection: '$collectionName', _id: ${_id}", result)
    }
  }

  override def ids(log: Log): Seq[Long] = {
    log.debugElapsed {
      val future = collection.find[Id]().projection(fields(include("_id"))).toFuture()
      val docs = Await.result(future, Duration(15, TimeUnit.MINUTES))
      (s"collection: '$collectionName', ids: ${docs.size}", docs.map(_._id))
    }
  }

  override def stringIds(log: Log): Seq[String] = {
    log.debugElapsed {
      val future = collection.find[StringId]().projection(fields(include("_id"))).toFuture()
      val docs = Await.result(future, Duration(15, TimeUnit.MINUTES))
      (s"collection: '$collectionName', ids: ${docs.size}", docs.map(_._id))
    }
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
        val result = Await.result(future, Duration(1, TimeUnit.MINUTES))
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
      val count = Await.result(future, Duration(1, TimeUnit.MINUTES))
      val message = s"countDocuments - collection: '$collectionName' : $count"
      (message, count)
    }
  }

  override def countDocuments(filter: Bson, log: Log): Long = {
    log.debugElapsed {
      val future = collection.countDocuments(filter).toFuture()
      val count = Await.result(future, Duration(1, TimeUnit.MINUTES))
      val message = s"countDocuments - collection: '$collectionName' : $count"
      (message, count)
    }
  }

  private def collectionName: String = collection.namespace.getCollectionName
}
