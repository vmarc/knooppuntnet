package kpn.core.mongo.changes

import kpn.api.common.network.NetworkInfo
import kpn.core.database.doc.NetworkDoc
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
      val networkId = network.attributes.id
      val id = s"network:$networkId"
      val doc = NetworkDoc(id, network)
      val filter = equal("_id", id)
      val collection = database.getCollection[NetworkDoc]("networks")
      val future = collection.replaceOne(filter, doc, ReplaceOptions().upsert(true)).toFuture()
      val result = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"save network $networkId", result)
    }
  }
}
