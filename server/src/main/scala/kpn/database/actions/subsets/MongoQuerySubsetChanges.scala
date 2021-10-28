package kpn.database.actions.subsets

import kpn.api.common.ChangeSetSummary
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.custom.Subset
import kpn.core.util.Log
import kpn.database.actions.subsets.MongoQuerySubsetChanges.log
import kpn.database.base.Database
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

object MongoQuerySubsetChanges {
  private val log = Log(classOf[MongoQuerySubsetChanges])
}

class MongoQuerySubsetChanges(database: Database) {

  def execute(subset: Subset, parameters: ChangesParameters): Seq[ChangeSetSummary] = {

    val filterElements = Seq(
      Some(equal("subsets.country", subset.country.domain)),
      Some(equal("subsets.networkType", subset.networkType.name)),
      if (parameters.impact) {
        Some(equal("impact", true))
      }
      else {
        None
      },
      parameters.year.map(year => equal("key.time.year", year.toInt)),
      parameters.month.map(month => equal("key.time.month", month.toInt)),
      parameters.day.map(day => equal("key.time.day", day.toInt)),
    ).flatten

    val pipeline = Seq(
      filter(
        and(filterElements: _*)
      ),
      sort(orderBy(descending("key.time"))),
      skip((parameters.pageIndex * parameters.itemsPerPage).toInt),
      limit(parameters.itemsPerPage.toInt),
      project(
        fields(
          excludeId()
        )
      )
    )

    log.debugElapsed {
      val changes = database.changes.aggregate[ChangeSetSummary](pipeline, log)
      val result = s"subset ${subset.name} changes: ${changes.size}"
      (result, changes)
    }
  }
}
