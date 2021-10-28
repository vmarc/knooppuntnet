package kpn.database.actions.statistics

import kpn.api.common.statistics.StatisticValues
import kpn.database.actions.statistics.MongoQueryStatistics.log
import kpn.database.base.Database
import kpn.database.base.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Accumulators.push
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.merge
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.MergeOptions
import org.mongodb.scala.model.MergeOptions.WhenMatched
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

object MongoQueryStatistics {

  private val log = Log(classOf[MongoQueryStatistics])

  def groupValues(database: Database, name: String): Seq[Bson] = {
    Seq(
      sort(orderBy(ascending("_id"))),
      project(
        fields(
          computed(
            "values",
            fields(
              computed("country", "$_id.country"),
              computed("networkType", "$_id.networkType"),
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

  def execute(): Seq[StatisticValues] = {
    log.debugElapsed {
      val values = database.statistics.findAll(log).sortBy(_._id)
      (s"${values.size} values", values)
    }
  }
}
