package kpn.database.actions.routes

import kpn.core.util.Log
import kpn.database.actions.routes.MongoQueryRouteDetailsData.log
import kpn.database.base.Database
import kpn.database.base.MongoProjections.arraySize
import kpn.database.base.Types.MongoPipeline
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryRouteDetailsData {
  private val log = Log(classOf[MongoQueryRouteDetailsData])
}

class MongoQueryRouteDetailsData(database: Database) {

  def execute(routeId: Long): Option[RouteDetailsData] = {
    log.debugElapsed {
      val pipeline = buildPipeline(routeId)
      val details = database.routes.optionAggregate[RouteDetailsData](pipeline, log)
      ("details", details)
    }
  }

  private def buildPipeline(routeId: Long): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("_id", routeId),
          equal("active", true),
        )
      ),
      project(
        fields(
          excludeId(),
          include("id"),
          include("active"),
          include("summary"),
          include("proposed"),
          include("version"),
          include("changeSetId"),
          include("lastUpdated"),
          include("lastSurvey"),
          include("facts"),
          include("unexpectedNodeIds"),
          include("unexpectedRelationIds"),
          include("memberCount"),
          arraySize("segmentCount", "$segments"),
          arraySize("pathCount", "$paths"),
          include("nameDerivedFromNodes"),
          include("nodes"),
          include("bounds"),
          include("routeIds"),
          include("relationCount"),
          include("relationLevels"),
          include("parentRoutes"),
          include("networkReferences"),
          include("locationAnalysis"),
        )
      )
    )
  }
}
