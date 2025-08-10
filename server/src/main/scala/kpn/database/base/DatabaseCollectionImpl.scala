package kpn.database.base

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.ReplaceOneModel
import com.mongodb.client.model.ReplaceOptions
import kpn.api.base.WithId
import kpn.api.base.WithObjectId
import kpn.api.base.WithStringId
import kpn.core.util.Log
import kpn.core.util.Util.seqToList
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.Types.MongoPipeline
import kpn.database.util.Mongo
import org.bson.conversions.Bson
import org.bson.types.ObjectId

import scala.concurrent.duration.Duration
import scala.jdk.CollectionConverters.IterableHasAsScala

class DatabaseCollectionImpl[TDocument](collection: MongoCollection[TDocument]) extends DatabaseCollection[TDocument] {

  override def native: MongoCollection[TDocument] = collection

  private val documentClass: Class[TDocument] = collection.getDocumentClass

  override def aggregate[TResult](
    pipeline: MongoPipeline,
    resultClass: Class[TResult],
    log: Log,
    allowDiskUse: Boolean,
    duration: Duration
  ): Seq[TResult] = {
    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }
    collection.aggregate(seqToList(pipeline), resultClass).allowDiskUse(allowDiskUse).asScala.toSeq
  }

  override def optionAggregate[TResult](
    pipeline: MongoPipeline,
    resultClass: Class[TResult],
    log: Log,
    duration: Duration
  ): Option[TResult] = {
    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }
    collection.aggregate(seqToList(pipeline), resultClass).asScala.headOption
  }

  override def stringPipelineAggregate[TResult](
    pipelineString: String,
    pipelineArgs: Map[String, String],
    resultClass: Class[TResult],
    log: Log,
    duration: Duration
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

  override def findOne(
    filter: Bson,
    log: Log
  ): Option[TDocument] = {
    log.debugElapsed {
      val doc = collection.find(filter, documentClass).asScala.headOption
      (s"find - collection: '$collectionName'", doc)
    }
  }

  override def find(
    filter: Bson,
    log: Log
  ): Seq[TDocument] = {
    log.debugElapsed {
      val docs = collection.find(filter, documentClass).asScala.toSeq
      (s"find - collection: '$collectionName', docs= ${docs.size}", docs)
    }
  }

  override def findById(
    _id: Long,
    log: Log
  ): Option[TDocument] = {
    log.debugElapsed {
      val doc = collection.find(equal("_id", _id), documentClass).asScala.headOption
      (s"findById - collection: '$collectionName', _id: ${_id}", doc)
    }
  }

  override def findByStringId(
    _id: String,
    log: Log
  ): Option[TDocument] = {
    log.debugElapsed {
      val doc = collection.find(equal("_id", _id), documentClass).asScala.headOption
      (s"findById - collection: '$collectionName', _id: ${_id}", doc)
    }
  }

  override def findByObjectId(
    objectId: ObjectId,
    log: Log
  ): Option[TDocument] = {
    log.debugElapsed {
      val doc = collection.find(equal("_id", objectId), documentClass).asScala.headOption
      (s"findByObjectId - collection: '$collectionName', _id: $objectId", doc)
    }
  }

  override def findByIds(
    ids: Seq[Long],
    log: Log
  ): Seq[TDocument] = {
    if (ids.nonEmpty) {
      log.debugElapsed {
        val filter = Filters.in("_id", ids: _*)
        val docs = collection.find(filter, documentClass).asScala.toSeq
        (s"findByIds - collection: '$collectionName', ids: ${ids.mkString(", ")}", docs)
      }
    }
    else {
      Seq.empty
    }
  }

  override def findAll(log: Log): Seq[TDocument] = {
    log.debugElapsed {
      val docs = collection.find(documentClass).asScala.toSeq
      (s"find - collection: '$collectionName', docs: ${docs.size}", docs)
    }
  }

  override def save(doc: TDocument, log: Log): Unit = {
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

  override def bulkSave(docs: Seq[TDocument], log: Log): Unit = {
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

  override def delete(_id: Long, log: Log): Unit = {
    log.debugElapsed {
      val filter = equal("_id", _id)
      val result = collection.deleteOne(filter)
      (s"delete - collection: '$collectionName', _id: ${_id}", result)
    }
  }

  override def deleteByStringId(_id: String, log: Log): Unit = {
    log.debugElapsed {
      val filter = equal("_id", _id)
      val result = collection.deleteOne(filter)
      (s"delete - collection: '$collectionName', _id: ${_id}", result)
    }
  }

  override def deleteByObjectId(objectId: ObjectId, log: Log): Unit = {
    log.debugElapsed {
      val filter = equal("_id", objectId)
      val result = collection.deleteOne(filter)
      (s"delete - collection: '$collectionName', _id: ${objectId.toHexString}", result)
    }
  }

  override def deleteMany(filter: Bson, log: Log): Unit = {
    log.debugElapsed {
      val result = collection.deleteMany(filter)
      (s"deleteMany - collection: '$collectionName'", result)
    }
  }

  override def ids(log: Log): Seq[Long] = {
    log.debugElapsed {
      val docs = collection.find(classOf[Id]).projection(fields(include("_id"))).asScala.toSeq
      (s"collection: '$collectionName', ids: ${docs.size}", docs.map(_._id))
    }
  }

  override def stringIds(log: Log): Seq[String] = {
    log.debugElapsed {
      val docs = collection.find[StringId](classOf[StringId]).projection(fields(include("_id"))).asScala.toSeq
      (s"collection: '$collectionName', ids: ${docs.size}", docs.map(_._id))
    }
  }

  override def objectIds(log: Log): Seq[ObjectId] = {
    aggregate(Seq(project(fields(include("_id")))), classOf[ObjectIdId]).map(_._id)
  }

  override def insertMany(docs: Seq[TDocument], log: Log): Unit = {
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

  override def countDocuments(log: Log): Long = {
    log.debugElapsed {
      val count = collection.countDocuments()
      val message = s"countDocuments - collection: '$collectionName' : $count"
      (message, count)
    }
  }

  override def countFilteredDocuments(filter: Bson, log: Log): Long = {
    log.debugElapsed {
      val count = collection.countDocuments(filter)
      val message = s"countDocuments - collection: '$collectionName' : $count"
      (message, count)
    }
  }

  override def updateOne(filter: Bson, update: Bson, log: Log): Unit = {
    collection.updateOne(filter, update)
  }

  override def drop(log: Log): Unit = {
    collection.drop()
  }

  private def collectionName: String = collection.getNamespace.getCollectionName
}
