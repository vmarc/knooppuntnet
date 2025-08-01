package kpn.database.actions.routes

import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.base.Types.MongoPipeline
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryRouteActiveIds {
  private val log = Log(classOf[MongoQueryRouteSegments])
}

class MongoQueryRouteActiveIds(database: Database) {

  def execute(routeIds: Seq[Long], log: Log = MongoQueryRouteActiveIds.log): Seq[Long] = {
    log.debugElapsed {
      val pipeline = buildPipeline(routeIds)
      val ids = database.baseRoutes.aggregate[Id](pipeline, log).map(_._id)
      (s"${ids.size} route ids", ids)
    }
  }

  private def buildPipeline(routeIds: Seq[Long]): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          in("_id", routeIds: _*)
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
