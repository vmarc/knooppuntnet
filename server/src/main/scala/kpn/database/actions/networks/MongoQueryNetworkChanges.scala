package kpn.database.actions.networks

import com.mongodb.client.model.Aggregates.limit
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.skip
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Sorts.descending
import com.mongodb.client.model.Sorts.orderBy
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.filter.ChangesParameters
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.database.util.Mongo

class MongoQueryNetworkChanges(database: Database) {

  private val log = Log(classOf[MongoQueryNetworkChanges])

  def execute(networkId: Long, parameters: ChangesParameters): Seq[NetworkChange] = {
    log.debugElapsed {
      val pipeline = buildPipeline(networkId, parameters)
      if (log.isTraceEnabled) {
        log.trace(Mongo.pipelineString(pipeline))
      }
      val docs = database.networkChanges.aggregate(pipeline, classOf[NetworkChange])
      (s"${docs.size} network changes", docs)
    }
  }

  private def buildPipeline(networkId: Long, parameters: ChangesParameters): MongoPipeline = {

    val filterElements = Seq(
      Some(equal("key.elementId", networkId)),
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
            "key.time",
          )
        )
      ),
      skip((parameters.pageSize * parameters.pageIndex).toInt),
      limit(parameters.pageSize.toInt),
      project(
        fields(
          excludeId()
        )
      )
    )
  }
}
