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
import kpn.database.base.Types.MongoPipeline

class OrphanNodeUpdater_ReferencesInRoutes(database: Database, log: Log) {

  def execute(): Seq[Long] = {
    log.debugElapsed {
      val pipeline = buildPipeline()
      val ids = database.baseRoutes.aggregate(pipeline, classOf[Id], log).map(_._id).distinct
      (s"${ids.size} nodes referenced in routes", ids)
    }
  }

  private def buildPipeline(): MongoPipeline = {
    Seq(
      filter(
        equal("active", true),
      ),
      unwind("$nodeRefs"),
      project(
        fields(
          computed("_id", "$nodeRefs")
        )
      )
    )
  }
}
