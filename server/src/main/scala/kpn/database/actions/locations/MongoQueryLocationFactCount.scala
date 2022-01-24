package kpn.database.actions.locations

import kpn.api.custom.NetworkType
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.actions.locations.MongoQueryLocationFactCount.log
import kpn.database.base.CountResult
import kpn.database.base.Database
import kpn.database.util.Mongo
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.model.Accumulators.sum
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
      query.execute(NetworkType.hiking, "de")
    }
  }
}

class MongoQueryLocationFactCount(database: Database) {

  def execute(networkType: NetworkType, locationName: String): Long = {

    val nodeFactsPipeline = Seq(
      filter(
        and(
          equal("labels", Label.active),
          equal("labels", Label.networkType(networkType)),
          equal("labels", Label.location(locationName)),
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

    val routeFactPipeline = Seq(
      filter(
        and(
          equal("labels", Label.active),
          equal("labels", Label.networkType(networkType)),
          equal("labels", Label.location(locationName)),
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
      Seq(unionWith("routes", routeFactPipeline: _*))
    ).flatten

    log.debugElapsed {
      val countResults = database.nodes.aggregate[CountResult](pipeline, log)
      val factCount = countResults.map(_.count).sum
      (s"location '$locationName' fact count: $factCount", factCount)
    }
  }
}
