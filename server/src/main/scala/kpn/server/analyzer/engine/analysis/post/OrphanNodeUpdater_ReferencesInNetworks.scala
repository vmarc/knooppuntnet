package kpn.server.analyzer.engine.analysis.post

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.fields
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter

class OrphanNodeUpdater_ReferencesInNetworks(database: Database, log: Log) {

  def execute(): Seq[Long] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(equal("active", true)),
        unwind("$members"),
        filter(
          and(
            equal("members.memberType", "node"),
          )
        ),
        project(
          fields(
            computed("_id", "$members.ref")
          )
        )
      )
      val ids = database.baseNetworks.aggregate(pipeline, classOf[Id], log).map(_._id).distinct
      (s"${ids.size} nodes referenced in networks", ids)
    }
  }
}
