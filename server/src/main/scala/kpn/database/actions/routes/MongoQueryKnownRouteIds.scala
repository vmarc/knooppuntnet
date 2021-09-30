package kpn.database.actions.routes

import kpn.database.base.Database
import kpn.database.base.Id
import kpn.core.util.Log
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryKnownRouteIds {
  private val log = Log(classOf[MongoQueryKnownRouteIds])
}

class MongoQueryKnownRouteIds(database: Database) {

  def execute(routeIds: Seq[Long], log: Log = MongoQueryKnownRouteIds.log): Seq[Long] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          in("_id", routeIds: _*),
        ),
        project(
          fields(
            include("_id"),
          )
        )
      )
      val ids = database.routes.aggregate[Id](pipeline, log)
      (s"known route ids: ${ids.size}", ids.map(_._id))
    }
  }
}
