package kpn.database.actions.subsets

import com.mongodb.client.model.Aggregates.limit
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.skip
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Sorts.descending
import com.mongodb.client.model.Sorts.orderBy
import kpn.api.common.ChangeSetSummary
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.custom.Subset
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter

class MongoQuerySubsetChanges(database: Database) {

  private val log = Log(classOf[MongoQuerySubsetChanges])

  def execute(subset: Subset, parameters: ChangesParameters): Seq[ChangeSetSummary] = {

    val filterElements = Seq(
      Some(equal("subsets.country", subset.country.toString)),
      Some(equal("subsets.routeType", subset.routeType.toString)),
      Option.when(parameters.impact) {
        equal("impact", true)
      },
      parameters.year.map(year => equal("key.time.year", year.toInt)),
      parameters.month.map(month => equal("key.time.month", month.toInt)),
      parameters.day.map(day => equal("key.time.day", day.toInt)),
    ).flatten

    val pipeline = Seq(
      filter(
        and(filterElements *)
      ),
      sort(orderBy(descending("key.time"))),
      skip((parameters.pageSize * parameters.pageIndex).toInt),
      limit(parameters.pageSize.toInt),
      project(
        fields(
          excludeId()
        )
      )
    )

    log.debugElapsed {
      val changes = database.changes.aggregate(pipeline, classOf[ChangeSetSummary], log)
      val result = s"subset ${subset.name} changes: ${changes.size}"
      (result, changes)
    }
  }
}
