package kpn.database.base

import com.mongodb.client.ListIndexesIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.ReplaceOneModel
import com.mongodb.client.model.ReplaceOptions
import kpn.api.id.WithId
import kpn.api.id.WithObjectId
import kpn.api.id.WithStringId
import kpn.core.util.Log
import kpn.core.util.Util.seqToList
import kpn.database.base.DatabaseCollection.collectionLog
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.Types.MongoPipeline
import kpn.database.util.Mongo
import org.bson.Document
import org.bson.conversions.Bson
import org.bson.types.ObjectId

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import scala.jdk.CollectionConverters.IterableHasAsScala

object DatabaseCollection {
  private val collectionLog = Log(classOf[DatabaseCollection[?]])
}

class DatabaseCollection[TDocument](collection: MongoCollection[TDocument]) {

  def native: MongoCollection[TDocument] = collection

  def name: String = native.getNamespace.getCollectionName

  private val documentClass: Class[TDocument] = collection.getDocumentClass

  def isEmpty: Boolean = {
    countDocuments(collectionLog) == 0
  }

  def listIndexes(): ListIndexesIterable[Document] = {
    native.listIndexes()
  }

  def aggregate[TResult](
    pipeline: MongoPipeline,
    resultClass: Class[TResult],
    log: Log = collectionLog,
    allowDiskUse: Boolean = false,
    duration: Duration = Duration(120, TimeUnit.SECONDS)
  ): Seq[TResult] = {
    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }
    collection.aggregate(seqToList(pipeline), resultClass).allowDiskUse(allowDiskUse).asScala.toSeq
  }

  def aggregate[TResult](pipeline: MongoPipeline, resultClass: Class[TResult]): Seq[TResult] = {
    collection.aggregate(seqToList(pipeline), resultClass).asScala.toSeq
  }

  def optionAggregate[TResult](
    pipeline: MongoPipeline,
    resultClass: Class[TResult],
    log: Log = collectionLog,
    duration: Duration = Duration(120, TimeUnit.SECONDS)
  ): Option[TResult] = {
    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }
    collection.aggregate(seqToList(pipeline), resultClass).asScala.headOption
  }

  def stringPipelineAggregate[TResult](
    pipelineString: String,
    pipelineArgs: Map[String, String],
    resultClass: Class[TResult],
    log: Log = collectionLog,
    duration: Duration = Duration(120, TimeUnit.SECONDS)
  ): Seq[TResult] = {

    val pipelineStringWithArgs = pipelineArgs.foldLeft(pipelineString) { case (string, arg) =>
      string.replaceAll(arg._1, arg._2)
    }
    val pipeline = new MongoQuery().toPipeline(pipelineStringWithArgs)
    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }
    collection.aggregate(seqToList(pipeline), resultClass).asScala.toSeq
  }

  def findOne(
    filter: Bson,
    log: Log = collectionLog
  ): Option[TDocument] = {
    log.debugElapsed {
      val doc = collection.find(filter, documentClass).asScala.headOption
      (s"find - collection: '$collectionName'", doc)
    }
  }

  def find(
    filter: Bson,
    log: Log = collectionLog
  ): Seq[TDocument] = {
    log.debugElapsed {
      val docs = collection.find(filter, documentClass).asScala.toSeq
      (s"find - collection: '$collectionName', docs= ${docs.size}", docs)
    }
  }

  def findById(
    _id: Long,
    log: Log = collectionLog
  ): Option[TDocument] = {
    log.debugElapsed {
      val doc = collection.find(equal("_id", _id), documentClass).asScala.headOption
      (s"findById - collection: '$collectionName', _id: ${_id}", doc)
    }
  }

  def findByStringId(
    _id: String,
    log: Log = collectionLog
  ): Option[TDocument] = {
    log.debugElapsed {
      val doc = collection.find(equal("_id", _id), documentClass).asScala.headOption
      (s"findById - collection: '$collectionName', _id: ${_id}", doc)
    }
  }

  def findByObjectId(
    objectId: ObjectId,
    log: Log = collectionLog
  ): Option[TDocument] = {
    log.debugElapsed {
      val doc = collection.find(equal("_id", objectId), documentClass).asScala.headOption
      (s"findByObjectId - collection: '$collectionName', _id: $objectId", doc)
    }
  }

  def findByIds(
    ids: Seq[Long],
    log: Log = collectionLog
  ): Seq[TDocument] = {
    if (ids.nonEmpty) {
      log.debugElapsed {
        val filter = Filters.in("_id", ids *)
        val docs = collection.find(filter, documentClass).asScala.toSeq
        (s"findByIds - collection: '$collectionName', ids: ${ids.mkString(", ")}", docs)
      }
    }
    else {
      Seq.empty
    }
  }

  def findAll(log: Log = collectionLog): Seq[TDocument] = {
    log.debugElapsed {
      val docs = collection.find(documentClass).asScala.toSeq
      (s"find - collection: '$collectionName', docs: ${docs.size}", docs)
    }
  }

  def save(doc: TDocument, log: Log = collectionLog): Unit = {
    log.debugElapsed {

      val (id, filter) = doc match {
        case withId: WithId => (withId._id.toString, equal("_id", withId._id))
        case withStringId: WithStringId => (withStringId._id, equal("_id", withStringId._id))
        case withObjectId: WithObjectId => (withObjectId._id, equal("_id", withObjectId._id))
        case _ => throw new IllegalArgumentException("document does not have een id")
      }
      val result = collection.replaceOne(filter, doc, new ReplaceOptions().upsert(true))
      (s"save - collection: '$collectionName', _id: $id", result)
    }
  }

  def bulkSave(docs: Seq[TDocument], log: Log = collectionLog): Unit = {
    if (docs.nonEmpty) {
      val requests = docs.map { doc =>
        val filter = doc match {
          case withId: WithId => equal("_id", withId._id)
          case withStringId: WithStringId => equal("_id", withStringId._id)
          case _ => throw new IllegalArgumentException("document does not have een id")
        }
        new ReplaceOneModel[TDocument](filter, doc, new ReplaceOptions().upsert(true))
      }
      val result = native.bulkWrite(seqToList(requests))
      // TODO interprete result?
    }
  }

  def delete(_id: Long, log: Log = collectionLog): Unit = {
    log.debugElapsed {
      val filter = equal("_id", _id)
      val result = collection.deleteOne(filter)
      (s"delete - collection: '$collectionName', _id: ${_id}", result)
    }
  }

  def deleteByStringId(_id: String, log: Log = collectionLog): Unit = {
    log.debugElapsed {
      val filter = equal("_id", _id)
      val result = collection.deleteOne(filter)
      (s"delete - collection: '$collectionName', _id: ${_id}", result)
    }
  }

  def deleteByObjectId(objectId: ObjectId, log: Log = collectionLog): Unit = {
    log.debugElapsed {
      val filter = equal("_id", objectId)
      val result = collection.deleteOne(filter)
      (s"delete - collection: '$collectionName', _id: ${objectId.toHexString}", result)
    }
  }

  def deleteMany(filter: Bson, log: Log = collectionLog): Unit = {
    log.debugElapsed {
      val result = collection.deleteMany(filter)
      (s"deleteMany - collection: '$collectionName'", result)
    }
  }

  def ids(log: Log = collectionLog): Seq[Long] = {
    log.debugElapsed {
      val docs = collection.find(classOf[Id]).projection(fields(include("_id"))).asScala.toSeq
      (s"collection: '$collectionName', ids: ${docs.size}", docs.map(_._id))
    }
  }

  def stringIds(log: Log = collectionLog): Seq[String] = {
    log.debugElapsed {
      val docs = collection.find[StringId](classOf[StringId]).projection(fields(include("_id"))).asScala.toSeq
      (s"collection: '$collectionName', ids: ${docs.size}", docs.map(_._id))
    }
  }

  def objectIds(log: Log = collectionLog): Seq[ObjectId] = {
    aggregate(Seq(project(fields(include("_id")))), classOf[ObjectIdId]).map(_._id)
  }

  def insertMany(docs: Seq[TDocument], log: Log = collectionLog): Unit = {
    if (docs.nonEmpty) {
      log.debugElapsed {
        val ids = docs.map {
          case withId: WithId => withId._id
          case withStringId: WithStringId => withStringId._id
          case _ => "?"
        }

        val result = collection.insertMany(seqToList(docs))
        val resultString = if (!result.wasAcknowledged()) {
          ", not acknowledged"
        }
        else if (docs.sizeIs != result.getInsertedIds.size()) {
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

  def countDocuments(log: Log = collectionLog): Long = {
    log.debugElapsed {
      val count = collection.countDocuments()
      val message = s"countDocuments - collection: '$collectionName' : $count"
      (message, count)
    }
  }

  def countFilteredDocuments(filter: Bson, log: Log = collectionLog): Long = {
    log.debugElapsed {
      val count = collection.countDocuments(filter)
      val message = s"countDocuments - collection: '$collectionName' : $count"
      (message, count)
    }
  }

  def updateOne(filter: Bson, update: Bson, log: Log = collectionLog): Unit = {
    collection.updateOne(filter, update)
  }

  def drop(log: Log = collectionLog): Unit = {
    collection.drop()
  }

  private def collectionName: String = collection.getNamespace.getCollectionName
}
