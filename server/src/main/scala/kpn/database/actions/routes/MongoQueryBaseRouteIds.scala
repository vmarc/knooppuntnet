package kpn.database.actions.routes

import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Id
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryBaseRouteIds {
  private val log = Log(classOf[MongoQueryBaseRouteIds])
}

class MongoQueryBaseRouteIds(database: Database) {

  def execute(log: Log = MongoQueryBaseRouteIds.log): Seq[Long] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          equal("active", true),
        ),
        project(
          fields(
            include("_id")
          )
        )
      )
      val ids = database.baseRoutes.aggregate[Id](pipeline, log).map(_._id)
      (s"${ids.size} base routes", ids)
    }
  }
}
