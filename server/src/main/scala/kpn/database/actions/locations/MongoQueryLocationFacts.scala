package kpn.database.actions.locations

import kpn.api.common.NetworkType
import kpn.api.common.location.LocationFact
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.actions.locations.MongoQueryLocationFacts.log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.analysis.location.LocationSubset
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.model.Accumulators.push
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unionWith
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.notEqual
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryLocationFacts {

  private val log = Log(classOf[MongoQueryLocationFacts])

  def main(args: Array[String]): Unit = {
    println("MongoQueryLocationFacts")
    Mongo.executeIn("kpn-prod") { database =>
      database.networks.findById(0)
      val query = new MongoQueryLocationFacts(database)
      val subset = LocationSubset("", NetworkType.hiking, Seq("fr"))
      val locationFacts = query.execute(subset)
      locationFacts.foreach { locationFact =>
        println(s"${locationFact.elementType} ${locationFact.fact.entryName}: ${locationFact.refs.map(_.name).mkString(", ")}")
      }
    }
  }
}

class MongoQueryLocationFacts(database: Database) {

  def execute(subset: LocationSubset): Seq[LocationFact] = {

    val mainFilter = filter(
      and(
        equal("labels", Label.active),
        equal("labels", Label.networkType(subset.networkType)),
        LocationQuery.locationFilter("labels", subset),
        equal("labels", Label.facts)
      )
    )

    val nodePipeline = Seq(
      mainFilter,
      unwind("$names"),
      filter(
        equal("names.networkType", subset.networkType.entryName)
      ),
      unwind("$facts"),
      project(
        fields(
          excludeId(),
          computed("fact", "$facts"),
          computed("ref.id", "$_id"),
          computed("ref.name", "$names.name"),
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
        equal("names.networkType", subset.networkType.entryName)
      ),
      unwind("$integrity.details"),
      filter(
        and(
          equal("integrity.details.networkType", subset.networkType.entryName),
          BsonDocument("""{$expr: { $ne: ["$integrity.details.expectedRouteCount", { "$size": "$integrity.details.routeRefs" }]}}""")
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

    val routePipeline = Seq(
      mainFilter,
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
          computed("fact", "$facts"),
          computed("ref.id", "$_id"),
          computed("ref.name", "$summary.name"),
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

    val pipeline = Seq(
      nodePipeline, // node facts
      Seq(unionWith("nodes", nodePipeline2: _*)),
      Seq(unionWith("routes", routePipeline: _*))
    ).flatten

    log.debugElapsed {
      val locationFactss = database.nodes.aggregate[LocationFact](pipeline, log)
      val facts = locationFactss.map { locationFacts =>
        val sortedRefs = locationFacts.refs.sortBy(ref => (ref.name, ref.id))
        locationFacts.copy(refs = sortedRefs)
      }
      (s"facts: ${facts.size}", facts)
    }
  }
}
