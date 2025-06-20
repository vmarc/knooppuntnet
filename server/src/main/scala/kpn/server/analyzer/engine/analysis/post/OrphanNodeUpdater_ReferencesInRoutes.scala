package kpn.server.analyzer.engine.analysis.post

import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.base.Types.MongoPipeline
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields

class OrphanNodeUpdater_ReferencesInRoutes(database: Database, log: Log) {

  def execute(): Seq[Long] = {
    log.debugElapsed {
      val pipeline = buildPipeline()
      val ids = database.baseRoutes.aggregate[Id](pipeline, log).map(_._id).distinct
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
