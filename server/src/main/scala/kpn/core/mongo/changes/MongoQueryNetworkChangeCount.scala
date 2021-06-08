package kpn.core.mongo.changes

import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Filters.equal

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class MongoQueryNetworkChangeCount(database: MongoDatabase) {

  private val log = Log(classOf[MongoQueryNetworkChangeCount])

  def execute(networkId: Long): Long = {
    log.debugElapsed {
      val future = database.getCollection("network-changes").countDocuments(equal("networkChange.key.elementId", networkId)).toFuture()
      val count = Await.result(future, Duration(60, TimeUnit.SECONDS))
      (s"network $networkId change count: $count", count)
    }
  }
}
