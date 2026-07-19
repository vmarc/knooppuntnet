package kpn.database.actions.locations

import com.mongodb.client.model.Accumulators.sum
import com.mongodb.client.model.Aggregates.group
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.orderBy
import kpn.api.common.Country
import kpn.api.common.RouteType
import kpn.core.doc.Label
import kpn.core.doc.LocationNodeCount
import kpn.core.util.Log
import kpn.database.actions.locations.MongoQueryLocationNodeCounts.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.database.util.Mongo

object MongoQueryLocationNodeCounts {

  private val log = Log(classOf[MongoQueryLocationNodeCounts])

  def main(args: Array[String]): Unit = {
    log.info("start")
    Mongo.executeIn("kpn-laptop") { database =>
      val query = new MongoQueryLocationNodeCounts(database)
      query.find(RouteType.hiking, Country.be)
      val counts = query.find(RouteType.hiking, Country.nl)
      counts.map(_.toString).foreach(log.info)
      log.info(s"counts: ${counts.size}")
    }
  }
}

class MongoQueryLocationNodeCounts(database: Database) {

  def find(routeType: RouteType, country: Country): Seq[LocationNodeCount] = {
    val pipeline = buildPipeline(routeType, country)
    log.debugElapsed {
      val counts = database.nodes.aggregate(pipeline, classOf[LocationNodeCount], log)
      (s"location node counts: ${counts.size}", counts)
    }
  }

  private def buildPipeline(routeType: RouteType, country: Country): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("labels", Label.location(country.entryName)),
          equal("labels", Label.routeType(routeType))
        )
      ),
      unwind("$base.locations"),
      group(
        "$base.locations",
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
  }
}
