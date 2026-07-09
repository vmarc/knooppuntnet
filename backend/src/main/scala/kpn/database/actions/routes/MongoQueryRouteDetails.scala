package kpn.database.actions.routes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.core.util.Log
import kpn.database.actions.routes.MongoQueryRouteDetails.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.MongoProjections.arraySize
import kpn.database.base.Types.MongoPipeline

object MongoQueryRouteDetails {
  private val log = Log(classOf[MongoQueryRouteDetails])
}

class MongoQueryRouteDetails(database: Database) {

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
          computed("id", "$_id"),
          include("active"),
          computed("raw", "$base.raw"),
          computed("countries", "$base.countries"),
          computed("nodeNetwork", "$base.nodeNetwork"),
          computed("routeTypes", "$base.routeTypes"),
          computed("scopes", "$base.scopes"),
          computed("name", "$base.name"),
          computed("meters", "$base.meters"),
          computed("wayCount", "$base.wayCount"),
          computed("proposed", "$base.proposed"),
          computed("lastUpdated", "$base.lastUpdated"),
          computed("lastSurvey", "$base.lastSurvey"),
          include("facts"),
          computed("unexpectedNodeIds", "$base.unexpectedNodeIds"),
          include("unexpectedRelationIds"),
          arraySize("memberCount", "$base.members"),
          arraySize("segmentCount", "$segments"),
          arraySize("pathCount", "$paths"),
          computed("nameDerivedFromNodes", "$base.nameDerivedFromNodes"),
          computed("nodes", "$base.nodes"),
          include("bounds"),
          include("routeIds"),
          include("relationCount"),
          include("relationLevels"),
          include("parentRoutes"),
          include("networkReferences"),
          computed("locationAnalysis", "$base.locationAnalysis"),
        )
      )
    )
  }
}
