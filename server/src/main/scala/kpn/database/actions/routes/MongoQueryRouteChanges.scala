package kpn.database.actions.routes

import com.mongodb.client.model.Aggregates.limit
import com.mongodb.client.model.Aggregates.skip
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Sorts.descending
import com.mongodb.client.model.Sorts.orderBy
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.changes.filter.ChangesParameters
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.database.util.Mongo

class MongoQueryRouteChanges(database: Database) {

  private val log = Log(classOf[MongoQueryRouteChanges])

  def execute(routeId: Long, parameters: ChangesParameters): Seq[RouteChange] = {
    val pipeline = buildPipeline(routeId, parameters)
    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }
    log.debugElapsed {
      val routeChanges = database.routeChanges.aggregate(pipeline, classOf[RouteChange])
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
