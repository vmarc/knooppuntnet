package kpn.database.actions.statistics

import kpn.api.common.statistics.StatisticValues
import kpn.database.actions.statistics.MongoQueryStatistics.groupValues
import kpn.database.actions.statistics.StatisticsUpdateSubsetOrphanNodeCount.log
import kpn.database.base.Database
import kpn.database.base.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.Document
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.group

object StatisticsUpdateSubsetOrphanNodeCount extends MongoQuery {
  private val log = Log(classOf[StatisticsUpdateSubsetOrphanNodeCount])
}

class StatisticsUpdateSubsetOrphanNodeCount(database: Database) {
  def execute(): Unit = {
    log.debugElapsed {
      val select = Seq(
        group(
          Document(
            "country" -> "$country",
            "networkType" -> "$networkType"
          ),
          sum("value", 1)
        )
      )
      val pipeline = select ++ groupValues(database, "OrphanNodeCount")
      val values = database.orphanNodes.aggregate[StatisticValues](pipeline, log)
      (s"${values.size} values", ())
    }
  }
}
