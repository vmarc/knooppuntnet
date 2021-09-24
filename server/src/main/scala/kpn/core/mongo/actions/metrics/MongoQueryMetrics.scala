package kpn.core.mongo.actions.metrics

import kpn.core.database.doc.ReplicationActionDoc
import kpn.core.mongo.Database
import kpn.core.mongo.MetricsDatabase
import kpn.core.mongo.actions.metrics.MongoQueryMetrics.log
import kpn.core.mongo.doc.NetworkDoc
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
