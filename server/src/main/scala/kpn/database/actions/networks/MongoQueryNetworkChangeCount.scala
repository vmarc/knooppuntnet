package kpn.database.actions.networks

import kpn.core.util.Log
import kpn.database.actions.networks.MongoQueryNetworkChangeCount.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal

object MongoQueryNetworkChangeCount {
  private val log = Log(classOf[MongoQueryNetworkChangeCount])
}

class MongoQueryNetworkChangeCount(database: Database) {

  def execute(networkId: Long): Long = {
    log.debugElapsed {
      val filter = equal("networkId", networkId)
      val count = database.networkChanges.countFilteredDocuments(filter)
      (s"network $networkId change count: $count", count)
    }
  }
}
