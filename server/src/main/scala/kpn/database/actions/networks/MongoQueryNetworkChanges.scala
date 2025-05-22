package kpn.database.actions.networks

import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.filter.ChangesParameters
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import kpn.database.util.Mongo
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

class MongoQueryNetworkChanges(database: Database) {

  private val log = Log(classOf[MongoQueryNetworkChanges])

  def execute(networkId: Long, parameters: ChangesParameters): Seq[NetworkChange] = {
    log.debugElapsed {
      val pipeline = buildPipeline(networkId, parameters)
      if (log.isTraceEnabled) {
        log.trace(Mongo.pipelineString(pipeline))
      }
      val docs = database.networkChanges.aggregate[NetworkChange](pipeline)
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
