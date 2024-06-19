package kpn.database.actions.locations

import kpn.api.custom.NetworkType
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.actions.locations.MongoQueryLocationFactCount.log
import kpn.database.base.CountResult
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.analysis.location.LocationSubset
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.count
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unionWith
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.notEqual
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields

object MongoQueryLocationFactCount {

  private val log = Log(classOf[MongoQueryLocationFactCount])

  def main(args: Array[String]): Unit = {
    println("MongoQueryLocationFactCount")
    Mongo.executeIn("kpn-test") { database =>
      database.networks.findById(0)
      val query = new MongoQueryLocationFactCount(database)
      val subset = LocationSubset("", NetworkType.hiking, Seq("de"))
      query.execute(subset)
    }
  }
}

class MongoQueryLocationFactCount(database: Database) {

  def execute(subset: LocationSubset): Long = {

    val nodeFactsPipeline = Seq(
      filter(
        and(
          equal("labels", Label.active),
          equal("labels", Label.networkType(subset.networkType)),
          LocationQuery.locationFilter("labels", subset),
          equal("labels", Label.facts)
        )
      ),
      project(
        fields(
          excludeId(),
          BsonDocument("""{"factCount": { "$size": "$facts" }}""")
        )
      ),
      group(
        null,
        sum("count", "$factCount")
      ),
      project(
        fields(
          excludeId(),
        )
      )
    )

    val nodePipeline2 = Seq(
      filter(
        and(
          equal("labels", Label.active),
          equal("labels", Label.networkType(subset.networkType)),
          LocationQuery.locationFilter("labels", subset),
        )
      ),
      unwind("$names"),
      filter(
        equal("names.networkType", subset.networkType.name)
      ),
      unwind("$integrity.details"),
      filter(
        and(
          equal("integrity.details.networkType", subset.networkType.name),
          BsonDocument("""{$expr: { $ne: ["$integrity.details.expectedRouteCount", { "$size": "$integrity.details.routeRefs" }]}}""")
        )
      ),
      count()
    )

    val routeFactPipeline = Seq(
      filter(
        and(
          equal("labels", Label.active),
          equal("labels", Label.networkType(subset.networkType)),
          LocationQuery.locationFilter("labels", subset),
          equal("labels", Label.facts)
        )
      ),
      unwind("$facts"),
      filter(
        and(
          notEqual("facts", "RouteBroken"),
          notEqual("facts", "RouteNotForward"),
          notEqual("facts", "RouteNotBackward"),
        )
      ),
      project(
        fields(
          excludeId(),
          BsonDocument("""{"factCount": {"$toInt": "1"}}""")
        )
      ),
      group(
        null,
        sum("count", "$factCount")
      ),
      project(
        fields(
          excludeId(),
        )
      )
    )

    val pipeline = Seq(
      nodeFactsPipeline,
      Seq(unionWith("nodes", nodePipeline2 *)),
      Seq(unionWith("routes", routeFactPipeline *))
    ).flatten

    log.debugElapsed {
      val countResults = database.nodes.aggregate[CountResult](pipeline, log)
      val factCount = countResults.map(_.count).sum
      (s"fact count: $factCount", factCount)
    }
  }
}
