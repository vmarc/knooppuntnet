package kpn.database.actions.metrics

import kpn.core.metrics.ReplicationActionDoc
import kpn.database.actions.metrics.MongoQueryMetrics.log
import kpn.database.base.Database
import kpn.database.base.MetricsDatabase
import kpn.core.doc.NetworkDoc
import kpn.core.util.Log
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.equal

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
      val network = database.replication.optionAggregate[ReplicationActionDoc](pipeline, log)
      (s"network $networkId", network)
    }


    None


  }
}
