package kpn.database.actions.locations

import com.mongodb.client.model.Accumulators.sum
import com.mongodb.client.model.Aggregates.group
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.regex
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.api.custom.Subset
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.base.CountResult
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.MongoAggregates.notEqual
import kpn.database.base.MongoAggregates.unionWith
import kpn.database.base.Types.MongoPipeline
import kpn.server.analyzer.engine.analysis.location.LocationSubset
import org.bson.BsonDocument
import org.bson.conversions.Bson

class MongoQueryLocations(database: Database) {
  private val log = Log(classOf[MongoQueryLocations])

  def distance(subset: LocationSubset): Long = {
    val pipeline = Seq(
      filter(
        and(
          equal("active", true),
          equal("labels", Label.routeType(subset.routeType)),
          LocationQuery.locationFilter("labels", subset),
        )
      ),
      group(
        "meters",
        sum("count", "$base.summary.meters")
      )
    )

    log.debugElapsed {
      val meters = database.routes.aggregate(pipeline, classOf[CountResult], log).map(_.count).sum
      (s"distance: $meters", meters)
    }
  }

  def execute(subset: Subset): Seq[LocationQueryResult] = {

    val pipeline = Seq(
      nodeCountPipeline(subset),
      Seq(
        unionWith("nodes", nodeFactCountsPipeline(subset)),
        unionWith("routes", routeCountPipeline(subset)),
        unionWith("routes", routeFactCountPipeline(subset)),
      ),
      Seq(
        group(
          "$name",
          sum("nodeCount", "$nodeCount"),
          sum("routeCount", "$routeCount"),
          sum("factCount", "$factCount")
        ),
        project(
          fields(
            excludeId(),
            computed("name", "$_id"),
            include("nodeCount"),
            include("routeCount"),
            include("factCount")
          )
        )
      ),
    ).flatten

    log.debugElapsed {
      val locations = database.nodes.aggregate(pipeline, classOf[LocationQueryResult], log)
      (s"locations: ${locations.size}", locations)
    }
  }

  private def nodeCountPipeline(subset: Subset): MongoPipeline = {
    selectLocations(subset) ++ Seq(
      groupByLocation,
      project(
        fields(
          excludeId(),
          computed("name", labelToLocation),
          computed("nodeCount", "$count"),
          computed("routeCount", "0"),
          computed("factCount", "0")
        )
      )
    )
  }

  private def nodeFactCountsPipeline(subset: Subset): MongoPipeline = {
    selectLocations(subset) ++ Seq(
      unwind("$facts"),
      groupByLocation,
      project(
        fields(
          excludeId(),
          computed("name", labelToLocation),
          computed("nodeCount", "0"),
          computed("routeCount", "0"),
          computed("factCount", "$count")
        )
      )
    )
  }

  private def routeCountPipeline(subset: Subset): MongoPipeline = {
    selectLocations(subset) ++ Seq(
      groupByLocation,
      project(
        fields(
          excludeId(),
          computed("name", labelToLocation),
          computed("nodeCount", "0"),
          computed("routeCount", "$count"),
          computed("factCount", "0")
        )
      )
    )
  }

  private def routeFactCountPipeline(subset: Subset): MongoPipeline = {
    selectLocations(subset) ++ Seq(
      unwind("$facts"),
      filter(
        and(
          notEqual("facts", "RouteBroken"),
          notEqual("facts", "RouteNotForward"),
          notEqual("facts", "RouteNotBackward"),
        )
      ),
      groupByLocation,
      project(
        fields(
          excludeId(),
          computed("name", labelToLocation),
          computed("nodeCount", "0"),
          computed("routeCount", "0"),
          computed("factCount", "$count")
        )
      )
    )
  }

  private def selectLocations(subset: Subset): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("labels", Label.routeType(subset.routeType)),
          equal("labels", Label.location(subset.country.entryName)),
        )
      ),
      unwind("$labels"),
      filter(
        regex("labels", s"^location-${subset.country.entryName}"),
      )
    )
  }

  private def groupByLocation: Bson = {
    group(
      "$labels",
      sum("count", 1)
    )
  }

  private def labelToLocation: Bson = {
    BsonDocument.parse(s"""{$$substr: ["$$_id", ${"location-".length}, 99]}""")
  }
}
