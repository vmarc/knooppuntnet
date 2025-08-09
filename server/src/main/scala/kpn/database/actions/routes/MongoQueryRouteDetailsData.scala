package kpn.database.actions.routes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.core.util.Log
import kpn.database.actions.routes.MongoQueryRouteDetailsData.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.MongoProjections.arraySize
import kpn.database.base.Types.MongoPipeline

object MongoQueryRouteDetailsData {
  private val log = Log(classOf[MongoQueryRouteDetailsData])
}

class MongoQueryRouteDetailsData(database: Database) {

  def execute(routeId: Long): Option[RouteDetailsData] = {
    log.debugElapsed {
      val pipeline = buildPipeline(routeId)
      val details = database.routes.optionAggregate(pipeline, classOf[RouteDetailsData], log)
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
