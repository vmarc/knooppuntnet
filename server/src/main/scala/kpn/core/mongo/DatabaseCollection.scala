package kpn.core.mongo

import kpn.core.mongo.DatabaseCollection.collectionLog
import kpn.core.util.Log
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.conversions.Bson

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import scala.reflect.ClassTag

object DatabaseCollection {
  private val collectionLog = Log(classOf[DatabaseCollection[_]])
}

trait DatabaseCollection[T] {

  def native: MongoCollection[T]

  def name: String = native.namespace.getCollectionName

  def aggregate[R: ClassTag](
    pipeline: Seq[Bson],
    log: Log = collectionLog,
    duration: Duration = Duration(30, TimeUnit.SECONDS)
  ): Seq[R]

  def optionAggregate[R: ClassTag](
    pipeline: Seq[Bson],
    log: Log = collectionLog,
    duration: Duration = Duration(30, TimeUnit.SECONDS)
  ): Option[R]

  def stringPipelineAggregate[R: ClassTag](
    pipelineString: String,
    pipelineArgs: Map[String, String],
    log: Log = collectionLog,
    duration: Duration = Duration(30, TimeUnit.SECONDS)
  ): Seq[R]

  def findOne[R: ClassTag](filter: Bson, log: Log = collectionLog): Option[R]

  def find[R: ClassTag](filter: Bson, log: Log = collectionLog): Seq[R]

  def findById(_id: Long, log: Log = collectionLog): Option[T]

  def findByStringId(_id: String, log: Log = collectionLog): Option[T]

  def findByIds(ids: Seq[Long], log: Log = collectionLog): Seq[T]

  def findAll(log: Log = collectionLog): Seq[T]

  def save(doc: T, log: Log = collectionLog): Unit

  def delete(_id: Long, log: Log = collectionLog): Unit

  def deleteByStringId(_id: String, log: Log = collectionLog): Unit

  def ids(log: Log = collectionLog): Seq[Long]

  def stringIds(log: Log = collectionLog): Seq[String]

  def insertMany(docs: Seq[T], log: Log = collectionLog): Unit

  def countDocuments(log: Log): Long

  def countDocuments(filter: Bson, log: Log = collectionLog): Long

}
