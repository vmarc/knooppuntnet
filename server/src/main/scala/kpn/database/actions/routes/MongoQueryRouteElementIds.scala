package kpn.database.actions.routes

import kpn.core.util.Log
import kpn.database.actions.routes.MongoQueryRouteElementIds.log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import kpn.server.analyzer.engine.changes.changes.ReferencedElementIds
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration

object MongoQueryRouteElementIds {
  private val log = Log(classOf[MongoQueryRouteElementIds])
}

class MongoQueryRouteElementIds(database: Database) {

  def execute(): Seq[ReferencedElementIds] = {
    log.debugElapsed {
      val pipeline = buildPipeline()
      val routeElementIdss = database.baseRoutes.aggregate[ReferencedElementIds](pipeline, log, duration = Duration(5, TimeUnit.MINUTES))
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
