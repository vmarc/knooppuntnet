package kpn.server.analyzer.engine.analysis.post

import com.mongodb.client.model.Aggregates.out
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.in
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.fields
import kpn.core.doc.OrphanRouteDoc
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.filter

class OrphanRouteUpdater_Update(database: Database, log: Log) {

  def execute(allOrphanRouteIds: Seq[Long]): Unit = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          in("_id", allOrphanRouteIds *)
        ),
        unwind("$summary.countries"),
        project(
          fields(
            computed("country", "$summary.countries"),
            computed("routeTypes", "$summary.routeTypes"),
            computed("name", "$summary.name"),
            computed("meters", "$summary.meters"),
            computed("facts", "$facts"),
            computed("lastSurvey", "$lastSurvey"),
            computed("lastUpdated", "$lastUpdated")
          )
        ),
        out(database.orphanRoutes.name)
      )
      val orphanRoutes = database.routes.aggregate(pipeline, classOf[OrphanRouteDoc], log)
      (s"${orphanRoutes.size} orphan routes", ())
    }
  }
}
