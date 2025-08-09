package kpn.server.analyzer.engine.analysis.post

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.fields
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter

class OrphanRouteUpdater_ReferencesInNetworks(database: Database, log: Log) {

  def execute(): Seq[Long] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(equal("active", true)),
        unwind("$routes"),
        project(
          fields(
            computed("_id", "$routes.id")
          )
        )
      )
      val ids = database.networks.aggregate(pipeline, classOf[Id], log).map(_._id).distinct
      (s"${ids.size} routes referenced in networks", ids)
    }
  }
}
