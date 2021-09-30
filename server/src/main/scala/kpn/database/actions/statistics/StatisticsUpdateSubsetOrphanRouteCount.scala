package kpn.database.actions.statistics

import kpn.api.common.statistics.StatisticValues
import kpn.database.actions.statistics.MongoQueryStatistics.groupValues
import kpn.database.actions.statistics.StatisticsUpdateSubsetOrphanRouteCount.log
import kpn.database.base.Database
import kpn.database.base.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.Document
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.group

object StatisticsUpdateSubsetOrphanRouteCount extends MongoQuery {
  private val log = Log(classOf[StatisticsUpdateSubsetOrphanRouteCount])
}

class StatisticsUpdateSubsetOrphanRouteCount(database: Database) {
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
      val pipeline = select ++ groupValues(database, "OrphanRouteCount")
      val values = database.orphanRoutes.aggregate[StatisticValues](pipeline)
      (s"${values.size} values", ())
    }
  }
}
