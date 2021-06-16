package kpn.core.mongo.actions.networks

import kpn.api.common.network.NetworkInfo
import kpn.core.mongo.Database
import kpn.core.mongo.actions.networks.MongoQueryNetwork.log
import kpn.core.util.Log

object MongoQueryNetwork {
  private val log = Log(classOf[MongoQueryNetwork])
}

class MongoQueryNetwork(database: Database) {

  def execute(networkId: Long): Option[NetworkInfo] = {
    log.debugElapsed {
      val network = database.networks.findById(networkId)
      (s"network $networkId", network)
    }
  }
}
