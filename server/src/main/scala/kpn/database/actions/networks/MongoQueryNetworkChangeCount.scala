package kpn.database.actions.networks

import kpn.database.actions.networks.MongoQueryNetworkChangeCount.log
import kpn.database.base.Database
import kpn.core.util.Log
import org.mongodb.scala.model.Filters.equal

object MongoQueryNetworkChangeCount {
  private val log = Log(classOf[MongoQueryNetworkChangeCount])
}

class MongoQueryNetworkChangeCount(database: Database) {

  def execute(networkId: Long): Long = {
    log.debugElapsed {
      val filter = equal("networkId", networkId)
      val count = database.networkInfoChanges.countDocuments(filter)
      (s"network $networkId change count: $count", count)
    }
  }
}
