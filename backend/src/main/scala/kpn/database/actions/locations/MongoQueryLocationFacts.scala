package kpn.database.actions.locations

import com.mongodb.client.model.Accumulators.push
import com.mongodb.client.model.Aggregates.group
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.elemMatch
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.api.common.RouteType
import kpn.api.common.location.LocationFact
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.actions.locations.MongoQueryLocationFacts.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.MongoAggregates.unionWith
import kpn.database.base.Types.MongoPipeline
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.analysis.location.LocationSubset
import org.bson.BsonDocument
import org.bson.conversions.Bson

object MongoQueryLocationFacts {

  private val log = Log(classOf[MongoQueryLocationFacts])

  def main(args: Array[String]): Unit = {
    log.info("start")
    Mongo.executeIn("kpn-laptop") { database =>
      database.networks.findById(0)
      val query = new MongoQueryLocationFacts(database)
      val subset = LocationSubset("", RouteType.hiking, Seq("fr"))
      val locationFacts = query.execute(subset)
      locationFacts.foreach { locationFact =>
        log.info(s"${locationFact.elementType} ${locationFact.fact.entryName}: ${locationFact.refs.map(_.name).mkString(", ")}")
      }
    }
  }
}

class MongoQueryLocationFacts(database: Database) {

  def execute(subset: LocationSubset): Seq[LocationFact] = {
    val pipeline = buildPipeline(subset)
    log.debugElapsed {
      val locationFacts = database.nodes.aggregate(pipeline, classOf[LocationFact], log)
      val facts = locationFacts.map { locationFact =>
        val sortedRefs = locationFact.refs.sortBy(ref => (ref.name, ref.id))
        locationFact.copy(refs = sortedRefs)
      }
      (s"facts: ${facts.size}", facts)
    }
  }

  private def buildPipeline(subset: LocationSubset): MongoPipeline = {
    val mainFilter = buildMainFilter(subset)
    val nodeFactPipeline = buildNodeFactPipeline(subset, mainFilter)
    val nodeIntegrityCheckFailedPipeline = buildNodeIntegrityCheckFailedPipeline(subset)
    val routePipeline = buildRouteFactPipeline(mainFilter)
    Seq(
      nodeFactPipeline, // node facts
      Seq(unionWith("nodes", nodeIntegrityCheckFailedPipeline)),
      Seq(unionWith("routes", routePipeline))
    ).flatten
  }

  private def buildMainFilter(subset: LocationSubset): Bson = {
    filter(
      and(
        equal("active", true),
        equal("labels", Label.routeType(subset.routeType)),
        LocationQuery.locationFilter("labels", subset),
        equal("labels", Label.facts)
      )
    )
  }

  private def buildNodeFactPipeline(subset: LocationSubset, mainFilter: Bson): MongoPipeline = {
    Seq(
      mainFilter,
      unwind("$base.names"),
      filter(
        equal("base.names.routeType", subset.routeType.entryName)
      ),
      unwind("$facts"),
      project(
        fields(
          excludeId(),
          computed("fact", "$facts"),
          computed("ref.id", "$_id"),
          computed("ref.name", "$base.names.name"),
        )
      ),
      group(
        "$fact",
        push("refs", "$ref")
      ),
      project(
        fields(
          excludeId(),
          computed("elementType", "node"),
          computed("fact", "$_id"),
          include("refs")
        )
      )
    )
  }

  private def buildNodeIntegrityCheckFailedPipeline(subset: LocationSubset): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("labels", Label.routeType(subset.routeType)),
          LocationQuery.locationFilter("labels", subset),
        )
      ),
      unwind("$base.names"),
      filter(
        elemMatch("base.names", equal("base.names.routeType", subset.routeType.entryName))
      ),
      unwind("$integrity.details"),
      filter(
        and(
          equal("integrity.details.routeType", subset.routeType.entryName),
          BsonDocument.parse("""{$expr: { $ne: ["$integrity.details.expectedRouteCount", { "$size": "$integrity.details.routeRefs" }]}}""")
        )
      ),
      project(
        fields(
          excludeId(),
          computed("elementType", "node"),
          computed("fact", "IntegrityCheckFailed"),
          computed("ref.id", "$_id"),
          computed("ref.name", "$names.name")
        )
      ),
      group(
        "$fact",
        push("refs", "$ref")
      ),
      project(
        fields(
          excludeId(),
          computed("elementType", "node"),
          computed("fact", "$_id"),
          include("refs")
        )
      )
    )
  }

  private def buildRouteFactPipeline(mainFilter: Bson): MongoPipeline = {
    Seq(
      mainFilter,
      unwind("$facts"),
      project(
        fields(
          excludeId(),
          computed("fact", "$facts"),
          computed("ref.id", "$_id"),
          computed("ref.name", "$base.name"),
        )
      ),
      group(
        "$fact",
        push("refs", "$ref")
      ),
      project(
        fields(
          excludeId(),
          computed("elementType", "route"),
          computed("fact", "$_id"),
          include("refs")
        )
      )
    )
  }
}
