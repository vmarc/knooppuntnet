package kpn.server.analyzer.engine.analysis.post

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.exists
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter

class OrphanRouteUpdater_AllRouteIds(database: Database, log: Log) {

  def execute(): Seq[Long] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("active", true),
            equal("summary.nodeNetwork", true),
            exists("summary.countries.0")
          )
        ),
        project(
          fields(
            include("_id")
          )
        )
      )
      val ids = database.routes.aggregate(pipeline, classOf[Id], log)
      (s"${ids.size} routes in total", ids.map(_._id))
    }
  }
}
