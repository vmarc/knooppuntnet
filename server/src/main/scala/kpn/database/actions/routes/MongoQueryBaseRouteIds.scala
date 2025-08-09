package kpn.database.actions.routes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQueryBaseRouteIds {
  private val log = Log(classOf[MongoQueryBaseRouteIds])
}

class MongoQueryBaseRouteIds(database: Database) {

  def execute(log: Log = MongoQueryBaseRouteIds.log): Seq[Long] = {
    log.debugElapsed {
      val pipeline = buildPipeline()
      val ids = database.baseRoutes.aggregate(pipeline, classOf[Id], log).map(_._id)
      (s"${ids.size} base routes", ids)
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
