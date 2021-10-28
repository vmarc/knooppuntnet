package kpn.server.analyzer.engine.analysis.post

import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Id
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields

class OrphanNodeUpdater_ReferencesInNetworks(database: Database, log: Log) {

  def execute(): Seq[Long] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(equal("active", true)),
        unwind("$nodes"),
        project(
          fields(
            computed("_id", "$nodes.id")
          )
        )
      )
      val ids = database.networkInfos.aggregate[Id](pipeline, log).map(_._id).distinct
      (s"${ids.size} nodes referenced in networks", ids)
    }
  }
}
