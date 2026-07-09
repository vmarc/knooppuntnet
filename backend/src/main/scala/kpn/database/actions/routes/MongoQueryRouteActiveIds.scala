package kpn.database.actions.routes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.in
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQueryRouteActiveIds {
  private val log = Log(classOf[MongoQueryRouteSegments])
}

class MongoQueryRouteActiveIds(database: Database) {

  def execute(routeIds: Seq[Long], log: Log = MongoQueryRouteActiveIds.log): Seq[Long] = {
    log.debugElapsed {
      val pipeline = buildPipeline(routeIds)
      val ids = database.baseRoutes.aggregate(pipeline, classOf[Id], log).map(_._id)
      (s"${ids.size} route ids", ids)
    }
  }

  private def buildPipeline(routeIds: Seq[Long]): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          in("_id", routeIds *)
        )
      ),
      project(
        fields(
          include("id"),
        )
      )
    )
  }
}
