package kpn.database.actions.locations

import com.mongodb.client.model.Accumulators.sum
import com.mongodb.client.model.Aggregates.count
import com.mongodb.client.model.Aggregates.group
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unionWith
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import kpn.api.common.RouteType
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.core.util.Util.seqToList
import kpn.database.actions.locations.MongoQueryLocationFactCount.log
import kpn.database.base.CountResult
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.MongoAggregates.notEqual
import kpn.database.base.MongoProjections.arraySize
import kpn.database.base.Types.MongoPipeline
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.analysis.location.LocationSubset
import org.bson.BsonDocument

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
      val countResults = database.nodes.aggregate(pipeline, classOf[CountResult], log)
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
      Seq(unionWith("nodes", seqToList(nodePipeline2))),
      Seq(unionWith("routes", seqToList(routeFactPipeline)))
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
          BsonDocument.parse("""{$expr: { $ne: ["$integrity.details.expectedRouteCount", { "$size": "$integrity.details.routeRefs" }]}}""")
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
          BsonDocument.parse("""{"factCount": {"$toInt": "1"}}""")
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
