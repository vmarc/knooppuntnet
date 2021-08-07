package kpn.core.mongo.actions.statistics

import kpn.api.common.statistics.StatisticValues
import kpn.core.mongo.Database
import kpn.core.mongo.actions.statistics.MongoQueryStatistics.groupValues
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetNodeCount.log
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.Document
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.exists

object StatisticsUpdateSubsetNodeCount extends MongoQuery {
  private val log = Log(classOf[StatisticsUpdateSubsetNodeCount])
}

class StatisticsUpdateSubsetNodeCount(database: Database) {

  def execute(): Unit = {
    log.debugElapsed {
      val select = Seq(
        filter(
          and(
            equal("labels", "active"),
            exists("country")
          )
        ),
        unwind("$names"),
        group(
          Document(
            "country" -> "$country",
            "networkType" -> "$names.networkType"
          ),
          sum("value", 1)
        )
      )
      val pipeline = select ++ groupValues(database, "NodeCount")
      val values = database.nodes.aggregate[StatisticValues](pipeline)
      (s"${values.size} values", ())
    }
  }

}
