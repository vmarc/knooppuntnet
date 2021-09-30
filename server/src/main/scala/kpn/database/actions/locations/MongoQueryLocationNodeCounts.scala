package kpn.database.actions.locations

import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.database.actions.locations.MongoQueryLocationNodeCounts.log
import kpn.database.base.Database
import kpn.core.doc.Label
import kpn.core.doc.LocationNodeCount
import kpn.database.util.Mongo
import kpn.core.util.Log
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

object MongoQueryLocationNodeCounts {

  private val log = Log(classOf[MongoQueryLocationNodeCounts])

  def main(args: Array[String]): Unit = {
    println("MongoQueryLocationNodeCount")
    Mongo.executeIn("kpn-test") { database =>
      val query = new MongoQueryLocationNodeCounts(database)
      query.find(NetworkType.hiking, Country.be)
      val counts = query.find(NetworkType.hiking, Country.nl)
      counts.foreach(println)
      println(s"counts: ${counts.size}")
    }
  }
}

class MongoQueryLocationNodeCounts(database: Database) {

  def find(networkType: NetworkType, country: Country): Seq[LocationNodeCount] = {

    val pipeline = Seq(
      filter(
        and(
          equal("labels", Label.active),
          equal("labels", Label.location(country.domain)),
          equal("labels", Label.networkType(networkType))
        )
      ),
      unwind("$locations"),
      group(
        "$locations",
        sum("count", 1)
      ),
      sort(orderBy(ascending("_id"))),
      project(
        fields(
          excludeId(),
          computed("name", "$_id"),
          include("count")
        )
      )
    )

    log.debugElapsed {
      val counts = database.nodes.aggregate[LocationNodeCount](pipeline, log)
      (s"location node counts: ${counts.size}", counts)
    }
  }
}
