package kpn.database.actions.changes

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
import kpn.core.util.Log
import kpn.database.actions.changes.MongoQueryChangeSetSummaries.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.database.util.Mongo

object MongoQueryChangeSetSummaries {

  private val log = Log(classOf[MongoQueryChangeSetSummaries])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      val parameters = ChangesParameters(
        pageSize = 15,
        year = Some(2017)
      )
      val query = new MongoQueryChangeSetSummaries(database)
      query.execute(parameters)
      val summaries = query.execute(parameters)
      summaries.map(_.key).foreach(println)
    }
  }
}

class MongoQueryChangeSetSummaries(database: Database) {

  def execute(parameters: ChangesParameters): Seq[ChangeSetSummary] = {

    val unfilteredPipeline: MongoPipeline = Seq(
      sort(orderBy(descending("key.time"))),
      skip((parameters.pageSize * parameters.pageIndex).toInt),
      limit(parameters.pageSize.toInt),
      project(
        fields(
          excludeId()
        )
      )
    )

    val filterElements = Seq(
      Option.when(parameters.impact) {
        equal("impact", true)
      },
      parameters.year.map(year => equal("key.time.year", year.toInt)),
      parameters.month.map(month => equal("key.time.month", month.toInt)),
      parameters.day.map(day => equal("key.time.day", day.toInt))
    ).flatten

    val pipeline = if (filterElements.nonEmpty) {
      Seq(filter(and(filterElements *))) ++ unfilteredPipeline
    }
    else {
      unfilteredPipeline
    }

    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }

    log.debugElapsed {
      val summaries = database.changes.aggregate(pipeline, classOf[ChangeSetSummary])
      (s"${summaries.size} changeset summaries", summaries)
    }
  }
}
