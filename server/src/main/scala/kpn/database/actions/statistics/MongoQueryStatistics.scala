package kpn.database.actions.statistics

import com.mongodb.client.model.Accumulators.push
import com.mongodb.client.model.Aggregates.group
import com.mongodb.client.model.Aggregates.merge
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.MergeOptions
import com.mongodb.client.model.MergeOptions.WhenMatched
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.orderBy
import kpn.core.util.Log
import kpn.database.actions.statistics.MongoQueryStatistics.log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline

object MongoQueryStatistics {

  private val log = Log(classOf[MongoQueryStatistics])

  def groupValues(database: Database, name: String): MongoPipeline = {
    Seq(
      sort(orderBy(ascending("_id"))),
      project(
        fields(
          computed(
            "values",
            fields(
              computed("country", "$_id.country"),
              computed("routeType", "$_id.base.routeType"),
              computed("value", "$value")
            )
          )
        )
      ),
      group(
        name,
        push("values", "$values")
      ),
      merge(
        database.statistics.name,
        new MergeOptions()
          .uniqueIdentifier("_id")
          .whenMatched(WhenMatched.REPLACE)
      )
    )
  }
}

class MongoQueryStatistics(database: Database) {

  def execute(): Seq[StatisticLongValues] = {
    log.debugElapsed {
      val values = database.statistics.findAll(log).sortBy(_._id)
      val sorted = values.map { statisticLongValues =>
        val sortedValues = statisticLongValues.values.sortBy(v => (v.country.entryName, v.routeType.entryName))
        statisticLongValues.copy(values = sortedValues)
      }
      (s"${sorted.size} values", sorted)
    }
  }
}
