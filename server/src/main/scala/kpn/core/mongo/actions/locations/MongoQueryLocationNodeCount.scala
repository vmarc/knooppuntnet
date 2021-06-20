package kpn.core.mongo.actions.locations

import kpn.api.custom.LocationNodesType
import kpn.api.custom.NetworkType
import kpn.core.mongo.Database
import kpn.core.mongo.actions.locations.MongoQueryLocationNodeCount.log
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.exists

object MongoQueryLocationNodeCount {
  private val log = Log(classOf[MongoQueryLocationNodeCount])

  def main(args: Array[String]): Unit = {
    println("MongoQueryLocationNodeCount")
    Mongo.executeIn("kpn-test") { database =>
      val query = new MongoQueryLocationNodeCount(database)
      query.execute(NetworkType.hiking, "Essen BE", LocationNodesType.all)
      query.execute(NetworkType.hiking, "Essen BE", LocationNodesType.all)
      query.execute(NetworkType.hiking, "Essen BE", LocationNodesType.survey)
      query.execute(NetworkType.hiking, "Essen BE", LocationNodesType.facts)
      query.execute(NetworkType.hiking, "be", LocationNodesType.all)
      query.execute(NetworkType.hiking, "be", LocationNodesType.survey)
      query.execute(NetworkType.hiking, "be", LocationNodesType.facts)
    }
  }
}

class MongoQueryLocationNodeCount(database: Database) {

  def execute(
    networkType: NetworkType,
    location: String,
    locationNodesType: LocationNodesType
  ): Long = {

    val filters = Seq(
      Some(equal("active", true)),
      Some(equal("names.networkType", networkType.name)),
      Some(equal("location.names", location)),
      locationNodesType match {
        case LocationNodesType.all => None
        case LocationNodesType.facts => Some(BsonDocument("{ facts: { $exists: true, $not: {$size: 0} } }"))
        case LocationNodesType.survey => Some(exists("lastSurvey"))
      }
    ).flatten

    val filter = and(filters: _*)

    database.nodes.countDocuments(filter, log)
  }
}
