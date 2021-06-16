package kpn.core.mongo.actions.networks

import kpn.core.mongo.Database
import kpn.core.mongo.actions.networks.MongoQueryNetworkChangeCount.log
import kpn.core.util.Log
import org.mongodb.scala.model.Filters.equal

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryNetworkChangeCount {
  private val log = Log(classOf[MongoQueryNetworkChangeCount])
}

class MongoQueryNetworkChangeCount(database: Database) {

  def execute(networkId: Long): Long = {
    log.debugElapsed {
      val filter = equal("key.elementId", networkId)
      val collection = database.database.getCollection("network-changes")
      val future = collection.countDocuments(filter).toFuture()
      val count = Await.result(future, Duration(60, TimeUnit.SECONDS))
      (s"network $networkId change count: $count", count)
    }
  }
}
