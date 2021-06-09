package kpn.core.mongo.changes

import kpn.api.common.network.NetworkInfo
import kpn.core.database.doc.NetworkDoc
import kpn.core.mongo.changes.MongoQueryNetwork.log
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
      val collection = database.getCollection[NetworkDoc]("networks")
      val future = collection.find[NetworkDoc](equal("_id", s"network:$networkId")).headOption()
      val network = Await.result(future, Duration(30, TimeUnit.SECONDS)).map(_.network)
      (s"network $networkId", network)
    }
  }
}
