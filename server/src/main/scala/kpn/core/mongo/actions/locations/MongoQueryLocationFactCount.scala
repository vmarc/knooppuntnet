package kpn.core.mongo.actions.locations

import kpn.api.custom.NetworkType
import kpn.core.mongo.CountResult
import kpn.core.mongo.Database
import kpn.core.mongo.actions.locations.MongoQueryLocationFactCount.log
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unionWith
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
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

    val subPipeline = Seq(
      filter(
        and(
          equal("labels", "active"),
          equal("labels", s"network-type-${networkType.name}"),
          equal("labels", s"location-${locationName}"),
          equal("labels", "facts")
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

    val pipeline = Seq(
      subPipeline, // node facts
      Seq(unionWith("routes", subPipeline: _*))
    ).flatten

    log.debugElapsed {
      val countResults = database.nodes.aggregate[CountResult](pipeline, log)
      val factCount = countResults.map(_.count).sum
      (s"location '$locationName' fact count: $factCount", factCount)
    }
  }
}
