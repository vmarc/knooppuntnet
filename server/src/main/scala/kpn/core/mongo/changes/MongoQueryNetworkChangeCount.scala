package kpn.core.mongo.changes

import kpn.core.mongo.changes.MongoQueryNetworkChangeCount.log
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Filters.equal

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryNetworkChangeCount {
  private val log = Log(classOf[MongoQueryNetworkChangeCount])
}

class MongoQueryNetworkChangeCount(database: MongoDatabase) {

  def execute(networkId: Long): Long = {
    log.debugElapsed {
      val filter = equal("key.elementId", networkId)
      val collection = database.getCollection("network-changes")
      val future = collection.countDocuments(filter).toFuture()
      val count = Await.result(future, Duration(60, TimeUnit.SECONDS))
      (s"network $networkId change count: $count", count)
    }
  }
}
