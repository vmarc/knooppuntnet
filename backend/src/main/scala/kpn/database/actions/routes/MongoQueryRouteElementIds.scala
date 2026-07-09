package kpn.database.actions.routes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.core.util.Log
import kpn.database.actions.routes.MongoQueryRouteElementIds.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.server.analyzer.engine.changes.changes.ReferencedElementIds

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration

object MongoQueryRouteElementIds {
  private val log = Log(classOf[MongoQueryRouteElementIds])
}

class MongoQueryRouteElementIds(database: Database) {

  def execute(): Seq[ReferencedElementIds] = {
    log.debugElapsed {
      val pipeline = buildPipeline()
      val routeElementIdss = database.baseRoutes.aggregate(
        pipeline,
        classOf[ReferencedElementIds],
        log,
        duration = Duration(10, TimeUnit.MINUTES)
      )
      (s"elementIds for active routes: ${routeElementIdss.size}", routeElementIdss)
    }
  }

  private def buildPipeline(): MongoPipeline = {
    Seq(
      filter(
        equal("active", true),
      ),
      project(
        fields(
          include("elementIds")
        )
      )
    )
  }
}
