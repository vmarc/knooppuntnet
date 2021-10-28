package kpn.database.actions.statistics

import kpn.api.common.statistics.StatisticValues
import kpn.core.util.Log
import kpn.database.actions.statistics.MongoQueryStatistics.groupValues
import kpn.database.actions.statistics.StatisticsUpdateSubsetChangeCount.log
import kpn.database.base.Database
import org.mongodb.scala.Document
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.unwind

object StatisticsUpdateSubsetChangeCount {
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
      val values = database.changes.aggregate[StatisticValues](pipeline)
      (s"${values.size} values", ())
    }
  }
}
