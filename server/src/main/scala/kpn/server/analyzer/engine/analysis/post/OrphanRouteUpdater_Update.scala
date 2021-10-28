package kpn.server.analyzer.engine.analysis.post

import kpn.core.doc.OrphanRouteDoc
import kpn.core.util.Log
import kpn.database.base.Database
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.out
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields

class OrphanRouteUpdater_Update(database: Database, log: Log) {

  def execute(allOrphanRouteIds: Seq[Long]): Unit = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          in("_id", allOrphanRouteIds: _*)
        ),
        project(
          fields(
            computed("country", "$summary.country"),
            computed("networkType", "$summary.networkType"),
            computed("name", "$summary.name"),
            computed("meters", "$summary.meters"),
            computed("facts", "$facts"),
            computed("lastSurvey", "$lastSurvey"),
            computed("lastUpdated", "$lastUpdated")
          )
        ),
        out(database.orphanRoutes.name)
      )
      val orphanRoutes = database.routes.aggregate[OrphanRouteDoc](pipeline, log)
      (s"${orphanRoutes.size} orphan routes", ())
    }
  }
}
