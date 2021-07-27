package kpn.core.mongo.actions.routes

import kpn.api.common.changes.details.RouteChange
import kpn.api.common.changes.filter.ChangesParameters
import kpn.core.mongo.Database
import kpn.core.mongo.actions.routes.MongoQueryRouteChanges.log
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.limit
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.skip
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Sorts.descending
import org.mongodb.scala.model.Sorts.orderBy

object MongoQueryRouteChanges {

  private val log = Log(classOf[MongoQueryRouteChanges])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      val query = new MongoQueryRouteChanges(database)
      //      query.execute(20628L, ChangesParameters(impact = true))
      //      query.execute(20628L, ChangesParameters(impact = true))
      //      query.execute(1599145L, ChangesParameters(impact = true))
      //      query.execute(1599145L, ChangesParameters(impact = true))
      val changes = query.execute(1125031L, ChangesParameters(itemsPerPage = 50))
      changes.map(_.key).foreach { key =>
        println(s"${key.timestamp.yyyymmddhhmm}  ${key.replicationNumber}  ${key.changeSetId}")
      }
    }
  }
}

class MongoQueryRouteChanges(database: Database) {

  def execute(routeId: Long, parameters: ChangesParameters): Seq[RouteChange] = {

    val filterElements = Seq(
      Some(equal("key.elementId", routeId)),
      if (parameters.impact) {
        Some(equal("impact", true))
      }
      else {
        None
      },
      parameters.year.map(year => equal("key.time.year", year.toInt)),
      parameters.month.map(month => equal("key.time.month", month.toInt)),
      parameters.day.map(day => equal("key.time.day", day.toInt))
    ).flatten

    val pipeline: Seq[Bson] = Seq(
      filter(
        and(filterElements: _*)
      ),
      sort(
        orderBy(
          descending(
            "key.time.year",
            "key.time.month",
            "key.time.day",
            "key.time.hour",
            "key.time.minute",
            "key.time.second"
          )
        )
      ),
      skip((parameters.itemsPerPage * parameters.pageIndex).toInt),
      limit(parameters.itemsPerPage.toInt),
      project(
        fields(
          excludeId()
        )
      )
    )

    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }

    log.debugElapsed {
      val routeChanges = database.routeChanges.aggregate[RouteChange](pipeline)
      (s"${routeChanges.size} route changes", routeChanges)
    }
  }
}
