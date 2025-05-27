package kpn.database.actions.routes

import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.base.Types.MongoPipeline
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryRouteIds {
  private val log = Log(classOf[MongoQueryRouteIds])
}

class MongoQueryRouteIds(database: Database) {

  def execute(log: Log = MongoQueryRouteIds.log): Seq[Long] = {
    log.debugElapsed {
      val pipeline = buildPipeline()
      val ids = database.routes.aggregate[Id](pipeline, log).map(_._id)
      (s"${ids.size} routes", ids)
    }
  }

  private def buildPipeline(): MongoPipeline = {
    Seq(
      filter(
        equal("active", true),
      ),
      project(
        fields(
          include("_id")
        )
      )
    )
  }
}
