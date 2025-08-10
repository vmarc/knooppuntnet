package kpn.database.base

import com.mongodb.client.ListIndexesIterable
import com.mongodb.client.MongoCollection
import kpn.core.util.Log
import kpn.database.base.DatabaseCollection.collectionLog
import kpn.database.base.Types.MongoPipeline
import org.bson.Document
import org.bson.conversions.Bson
import org.bson.types.ObjectId

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration

object DatabaseCollection {
  private val collectionLog = Log(classOf[DatabaseCollection[?]])
}

trait DatabaseCollection[TDocument] {

  def native: MongoCollection[TDocument]

  def name: String = native.getNamespace.getCollectionName

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
  ): Seq[TResult]

  def optionAggregate[TResult](
    pipeline: MongoPipeline,
    resultClass: Class[TResult],
    log: Log = collectionLog,
    duration: Duration = Duration(120, TimeUnit.SECONDS)
  ): Option[TResult]

  def stringPipelineAggregate[TResult](
    pipelineString: String,
    pipelineArgs: Map[String, String],
    resultClass: Class[TResult],
    log: Log = collectionLog,
    duration: Duration = Duration(120, TimeUnit.SECONDS)
  ): Seq[TResult]

  def findOne(
    filter: Bson,
    log: Log = collectionLog
  ): Option[TDocument]

  def find(filter: Bson, log: Log = collectionLog): Seq[TDocument]

  def findById(_id: Long, log: Log = collectionLog): Option[TDocument]

  def findByStringId(_id: String, log: Log = collectionLog): Option[TDocument]

  def findByObjectId(_id: ObjectId, log: Log = collectionLog): Option[TDocument]

  def findByIds(ids: Seq[Long], log: Log = collectionLog): Seq[TDocument]

  def findAll(log: Log = collectionLog): Seq[TDocument]

  def save(doc: TDocument, log: Log = collectionLog): Unit

  def bulkSave(docs: Seq[TDocument], log: Log = collectionLog): Unit

  def delete(_id: Long, log: Log = collectionLog): Unit

  def deleteByStringId(_id: String, log: Log = collectionLog): Unit

  def deleteByObjectId(_id: ObjectId, log: Log = collectionLog): Unit

  def deleteMany(filter: Bson, log: Log = collectionLog): Unit

  def ids(log: Log = collectionLog): Seq[Long]

  def stringIds(log: Log = collectionLog): Seq[String]

  def objectIds(log: Log = collectionLog): Seq[ObjectId]

  def insertMany(docs: Seq[TDocument], log: Log = collectionLog): Unit

  def countDocuments(log: Log = collectionLog): Long

  def countFilteredDocuments(filter: Bson, log: Log = collectionLog): Long

  def updateOne(filter: Bson, update: Bson, log: Log = collectionLog): Unit

  def drop(log: Log = collectionLog): Unit
}
