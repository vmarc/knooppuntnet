package kpn.database.actions.locations

import kpn.api.common.RouteType
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.actions.locations.MongoQueryLocationFactCount.log
import kpn.database.base.CountResult
import kpn.database.base.Database
import kpn.database.base.MongoProjections.arraySize
import kpn.database.base.Types.MongoPipeline
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
    log.info("start")
    Mongo.executeIn("kpn-laptop") { database =>
      database.networks.findById(0)
      val query = new MongoQueryLocationFactCount(database)
      val subset = LocationSubset("", RouteType.hiking, Seq("de"))
      log.infoElapsed {
        val count = query.execute(subset)
        (s"location fact count: $count", ())
      }
    }
  }
}

class MongoQueryLocationFactCount(database: Database) {

  def execute(subset: LocationSubset): Long = {
    val pipeline = buildPipeline(subset)
    log.debugElapsed {
      val countResults = database.nodes.aggregate[CountResult](pipeline, log)
      val factCount = countResults.map(_.count).sum
      (s"fact count: $factCount", factCount)
    }
  }

  private def buildPipeline(subset: LocationSubset): MongoPipeline = {

    val nodeFactsPipeline = buildNodeFactCountPipeline(subset)
    val nodePipeline2 = buildNodeIntegrityCheckFailedCountPipeline(subset)
    val routeFactPipeline = buildRouteFactCountPipeline(subset)

    Seq(
      nodeFactsPipeline,
      Seq(unionWith("nodes", nodePipeline2: _*)),
      Seq(unionWith("routes", routeFactPipeline: _*))
    ).flatten
  }

  private def buildNodeFactCountPipeline(subset: LocationSubset): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("labels", Label.routeType(subset.routeType)),
          LocationQuery.locationFilter("labels", subset),
          equal("labels", Label.facts)
        )
      ),
      project(
        fields(
          excludeId(),
          arraySize("factCount", "$facts")
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
  }

  private def buildNodeIntegrityCheckFailedCountPipeline(subset: LocationSubset): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("labels", Label.routeType(subset.routeType)),
          LocationQuery.locationFilter("labels", subset),
        )
      ),
      unwind("$names"),
      filter(
        equal("names.routeType", subset.routeType.entryName)
      ),
      unwind("$integrity.details"),
      filter(
        and(
          equal("integrity.details.routeType", subset.routeType.entryName),
          BsonDocument("""{$expr: { $ne: ["$integrity.details.expectedRouteCount", { "$size": "$integrity.details.routeRefs" }]}}""")
        )
      ),
      count()
    )
  }

  private def buildRouteFactCountPipeline(subset: LocationSubset): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("labels", Label.routeType(subset.routeType)),
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
  }
}
