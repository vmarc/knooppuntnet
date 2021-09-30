package kpn.database.actions.networks

import kpn.database.actions.networks.MongoQueryNetwork.log
import kpn.database.base.Database
import kpn.core.doc.NetworkDoc
import kpn.core.util.Log
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.equal

object MongoQueryNetwork {
  private val log = Log(classOf[MongoQueryNetwork])
}

class MongoQueryNetwork(database: Database) {

  def execute(networkId: Long): Option[NetworkDoc] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter( // can return both active and non-active networks
          equal("_id", networkId)
        )
      )
      val network = database.networks.optionAggregate[NetworkDoc](pipeline, log)
      (s"network $networkId", network)
    }
  }
}
