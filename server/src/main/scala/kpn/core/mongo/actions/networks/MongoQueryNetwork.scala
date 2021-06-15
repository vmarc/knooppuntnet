package kpn.core.mongo.actions.networks

import kpn.api.common.network.NetworkInfo
import kpn.core.mongo.actions.networks.MongoQueryNetwork.log
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Filters.equal

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryNetwork {
  private val log = Log(classOf[MongoQueryNetwork])
}

class MongoQueryNetwork(database: MongoDatabase) {

  def execute(networkId: Long): Option[NetworkInfo] = {
    log.debugElapsed {
      val collection = database.getCollection[NetworkInfo]("networks")
      val future = collection.find[NetworkInfo](equal("_id", networkId)).headOption()
      val network = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"network $networkId", network)
    }
  }
}
