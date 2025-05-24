package kpn.database.actions.routes

import kpn.api.common.changes.details.RouteChange
import kpn.api.common.changes.filter.ChangesParameters
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import kpn.database.util.Mongo
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.limit
import org.mongodb.scala.model.Aggregates.skip
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Sorts.descending
import org.mongodb.scala.model.Sorts.orderBy

class MongoQueryRouteChanges(database: Database) {

  private val log = Log(classOf[MongoQueryRouteChanges])

  def execute(routeId: Long, parameters: ChangesParameters): Seq[RouteChange] = {
    val pipeline = buildPipeline(routeId, parameters)
    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }
    log.debugElapsed {
      val routeChanges = database.routeChanges.aggregate[RouteChange](pipeline)
      (s"${routeChanges.size} route changes", routeChanges)
    }
  }

  private def buildPipeline(routeId: Long, parameters: ChangesParameters): MongoPipeline = {

    val filterElements = Seq(
      Some(equal("key.elementId", routeId)),
      Option.when(parameters.impact) {
        equal("impact", true)
      },
      parameters.year.map(year => equal("key.time.year", year.toInt)),
      parameters.month.map(month => equal("key.time.month", month.toInt)),
      parameters.day.map(day => equal("key.time.day", day.toInt))
    ).flatten

    Seq(
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
      skip((parameters.pageSize * parameters.pageIndex).toInt),
      limit(parameters.pageSize.toInt),
    )
  }
}
