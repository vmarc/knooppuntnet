package kpn.database.actions.statistics

import kpn.api.common.statistics.StatisticValues
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.actions.statistics.MongoQueryStatistics.groupValues
import kpn.database.actions.statistics.StatisticsUpdateSubsetRouteCount.log
import kpn.database.base.Database
import org.mongodb.scala.Document
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.exists

object StatisticsUpdateSubsetRouteCount {
  private val log = Log(classOf[StatisticsUpdateSubsetRouteCount])
}

class StatisticsUpdateSubsetRouteCount(database: Database) {

  def execute(): Unit = {
    log.debugElapsed {
      val select = Seq(
        filter(
          and(
            equal("labels", Label.active),
            exists("summary.country")
          )
        ),
        group(
          Document(
            "country" -> "$summary.country",
            "networkType" -> "$summary.networkType"
          ),
          sum("value", 1)
        )
      )
      val pipeline = select ++ groupValues(database, "RouteCount")
      val values = database.routes.aggregate[StatisticValues](pipeline)
      (s"${values.size} values", ())
    }
  }
}
