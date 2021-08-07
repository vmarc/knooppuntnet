package kpn.core.mongo.actions.statistics

import kpn.api.common.statistics.StatisticValues
import kpn.core.mongo.Database
import kpn.core.mongo.actions.statistics.MongoQueryStatistics.groupValues
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetChangeCount.log
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.Document
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.unwind

object StatisticsUpdateSubsetChangeCount extends MongoQuery {
  private val log = Log(classOf[StatisticsUpdateSubsetChangeCount])
}

class StatisticsUpdateSubsetChangeCount(database: Database) {
  def execute(): Unit = {
    log.debugElapsed {
      val select = Seq(
        unwind("$subsets"),
        group(
          Document(
            "country" -> "$subsets.country",
            "networkType" -> "$subsets.networkType"
          ),
          sum("value", 1)
        )
      )
      val pipeline = select ++ groupValues(database, "ChangeCount")
      val values = database.changeSetSummaries.aggregate[StatisticValues](pipeline)
      (s"${values.size} values", ())
    }
  }
}
