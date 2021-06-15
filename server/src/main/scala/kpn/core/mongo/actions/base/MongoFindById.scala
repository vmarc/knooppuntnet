package kpn.core.mongo.actions.base

import kpn.core.mongo.actions.base.MongoFindById.log
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Filters.equal

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.reflect.ClassTag

object MongoFindById {
  private val log = Log(classOf[MongoFindById])
}

class MongoFindById(database: MongoDatabase) {
  def execute[T: ClassTag](collectionName: String, _id: Long): Option[T] = {
    log.debugElapsed {
      val collection = database.getCollection[T](collectionName)
      val future = collection.find[T](equal("_id", _id)).headOption()
      val doc = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"collection: '$collectionName', _id: ${_id}", doc)
    }
  }
}
