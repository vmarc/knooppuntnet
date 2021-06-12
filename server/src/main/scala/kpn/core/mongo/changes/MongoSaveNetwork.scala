package kpn.core.mongo.changes

import kpn.api.common.network.NetworkInfo
import kpn.core.mongo.changes.MongoSaveNetwork.log
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.ReplaceOptions

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoSaveNetwork {
  private val log = Log(classOf[MongoSaveNetwork])
}

class MongoSaveNetwork(database: MongoDatabase) {

  def execute(network: NetworkInfo): Unit = {
    log.debugElapsed {
      val filter = equal("_id", network._id)
      val collection = database.getCollection[NetworkInfo]("networks")
      val future = collection.replaceOne(filter, network, ReplaceOptions().upsert(true)).toFuture()
      val result = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"save network ${network._id}", result)
    }
  }
}
