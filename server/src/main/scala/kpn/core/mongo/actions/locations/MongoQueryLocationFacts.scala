package kpn.core.mongo.actions.locations

import kpn.api.common.NodeName
import kpn.api.common.location.LocationFact
import kpn.api.custom.NetworkType
import kpn.core.mongo.Database
import kpn.core.mongo.actions.locations.MongoQueryLocationFacts.log
import kpn.core.mongo.doc.Label
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import org.mongodb.scala.model.Accumulators.push
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unionWith
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

case class LocationNodeFactDoc(
  factName: String,
  names: Seq[NodeName]
)

object MongoQueryLocationFacts {

  private val log = Log(classOf[MongoQueryLocationFacts])

  def main(args: Array[String]): Unit = {
    println("MongoQueryLocationFacts")
    Mongo.executeIn("kpn-test") { database =>
      database.networks.findById(0)
      val query = new MongoQueryLocationFacts(database)
      val locationFacts = query.execute(NetworkType.cycling, "de")
      locationFacts.foreach { locationFact =>
        println(s"${locationFact.elementType} ${locationFact.fact.name}: ${locationFact.refs.map(_.name).mkString(", ")}")
      }
    }
  }
}

class MongoQueryLocationFacts(database: Database) {

  def execute(networkType: NetworkType, locationName: String): Seq[LocationFact] = {

    val mainFilter = filter(
      and(
        equal("labels", Label.active),
        equal("labels", Label.networkType(networkType)),
        equal("labels", Label.location(locationName)),
        equal("labels", Label.facts)
      )
    )

    val nodePipeline = Seq(
      mainFilter,
      unwind("$names"),
      filter(
        equal("names.networkType", networkType.name)
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

    val routePipeline = Seq(
      mainFilter,
      unwind("$facts"),
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
      Seq(unionWith("routes", routePipeline: _*))
    ).flatten

    log.debugElapsed {
      val locationFactss = database.nodes.aggregate[LocationFact](pipeline, log)
      val facts = locationFactss.map { locationFacts =>
        val sortedRefs = locationFacts.refs.sortBy(ref => (ref.name, ref.id))
        locationFacts.copy(refs = sortedRefs)
      }
      (s"location '$locationName' facts: ${facts.size}", facts)
    }
  }
}
