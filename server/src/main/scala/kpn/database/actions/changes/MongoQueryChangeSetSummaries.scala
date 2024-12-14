package kpn.database.actions.changes

import kpn.api.common.ChangeSetSummary
import kpn.api.common.changes.filter.ChangesParameters
import kpn.database.actions.changes.MongoQueryChangeSetSummaries.log
import kpn.database.base.Database
import kpn.database.util.Mongo
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

    val unfilteredPipeline: Seq[Bson] = Seq(
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

    val pipeline = if (filterElements.nonEmpty) {
      Seq(filter(and(filterElements: _*))) ++ unfilteredPipeline
    }
    else {
      unfilteredPipeline
    }

    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }

    log.debugElapsed {
      val summaries = database.changes.aggregate[ChangeSetSummary](pipeline)
      (s"${summaries.size} changeset summaries", summaries)
    }
  }
}
