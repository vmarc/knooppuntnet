package kpn.database.actions.locations

import kpn.api.custom.Subset
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unionWith
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.notEqual
import org.mongodb.scala.model.Filters.regex
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

class MongoQueryLocations(database: Database) {
  private val log = Log(classOf[MongoQueryLocations])

  def execute(subset: Subset): Seq[LocationQueryResult] = {

    val pipeline = Seq(
      nodeCountPipeline(subset),
      Seq(
        unionWith("nodes", nodeFactCountsPipeline(subset): _*),
        unionWith("routes", routeCountPipeline(subset): _*),
        unionWith("routes", routeFactCountPipeline(subset): _*),
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
      val locations = database.nodes.aggregate[LocationQueryResult](pipeline, log)
      (s"locations: ${locations.size}", locations)
    }
  }

  private def nodeCountPipeline(subset: Subset): Seq[Bson] = {
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

  private def nodeFactCountsPipeline(subset: Subset): Seq[Bson] = {
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

  private def routeCountPipeline(subset: Subset): Seq[Bson] = {
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

  private def routeFactCountPipeline(subset: Subset): Seq[Bson] = {
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

  private def selectLocations(subset: Subset): Seq[Bson] = {
    Seq(
      filter(
        and(
          equal("labels", Label.active),
          equal("labels", Label.networkType(subset.networkType)),
          equal("labels", Label.location(subset.country.domain)),
        )
      ),
      unwind("$labels"),
      filter(
        regex("labels", s"^location-${subset.country.domain}"),
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
    BsonDocument(s"""{$$substr: ["$$_id", ${"location-".length}, 99]}""")
  }
}
