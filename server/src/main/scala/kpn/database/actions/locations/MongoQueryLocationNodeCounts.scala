package kpn.database.actions.locations

import kpn.api.common.Country
import kpn.api.common.RouteType
import kpn.core.doc.Label
import kpn.core.doc.LocationNodeCount
import kpn.core.util.Log
import kpn.database.actions.locations.MongoQueryLocationNodeCounts.log
import kpn.database.base.Database
import kpn.database.util.Mongo
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
      query.find(RouteType.hiking, Country.be)
      val counts = query.find(RouteType.hiking, Country.nl)
      counts.foreach(println)
      println(s"counts: ${counts.size}")
    }
  }
}

class MongoQueryLocationNodeCounts(database: Database) {

  def find(routeType: RouteType, country: Country): Seq[LocationNodeCount] = {

    val pipeline = Seq(
      filter(
        and(
          equal("active", true),
          equal("labels", Label.location(country.entryName)),
          equal("labels", Label.routeType(routeType))
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
