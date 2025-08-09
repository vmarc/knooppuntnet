package kpn.database.actions.metrics

import kpn.core.doc.NetworkDoc
import kpn.core.metrics.ReplicationActionDoc
import kpn.core.util.Log
import kpn.database.actions.metrics.MongoQueryMetrics.log
import kpn.database.base.MetricsDatabase
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter

object MongoQueryMetrics {
  private val log = Log(classOf[MongoQueryMetrics])
}

class MongoQueryMetrics(database: MetricsDatabase) {

  def execute(networkId: Long): Option[NetworkDoc] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter( // can return both active and non-active networks
          equal("_id", networkId)
        )
      )
      val network = database.replication.optionAggregate(pipeline, classOf[ReplicationActionDoc], log)
      (s"network $networkId", network)
    }

    None
  }
}
