package kpn.core.mongo.actions.statistics

import kpn.core.mongo.Database
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetChangeCount.log
import kpn.core.mongo.util.MongoProjections.concat
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.Document
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.merge
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.MergeOptions
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields

object StatisticsUpdateSubsetChangeCount extends MongoQuery {
  private val log = Log(classOf[StatisticsUpdateSubsetChangeCount])
}

class StatisticsUpdateSubsetChangeCount(database: Database) {
  def execute(): Unit = {
    log.debugElapsed {
      val pipeline = Seq(
        unwind("$subsets"),
        group(
          Document(
            "country" -> "$subsets.country",
            "networkType" -> "$subsets.networkType"
          ),
          sum("changeCount", 1)
        ),
        project(
          fields(
            concat("_id", "$_id.country", ":", "$_id.networkType", ":ChangeCount"),
            computed("country", "$_id.country"),
            computed("networkType", "$_id.networkType"),
            computed("name", "ChangeCount"),
            computed("value", "$changeCount")
          )
        ),
        merge(
          database.statisticsSubsetChangeCount.name,
          new MergeOptions().uniqueIdentifier("_id")
        )
      )

      val values = database.changeSetSummaries.aggregate[StatisticValue](pipeline)
      (s"${values.size} values", ())
    }
  }
}
