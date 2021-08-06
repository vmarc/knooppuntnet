package kpn.core.mongo.actions.statistics

import kpn.api.common.statistics.StatisticValue
import kpn.core.mongo.Database
import kpn.core.mongo.actions.statistics.MongoQueryStatistics.log
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.model.Aggregates.unionWith

object MongoQueryStatistics extends MongoQuery {
  private val log = Log(classOf[MongoQueryStatistics])
}

class MongoQueryStatistics(database: Database) {

  private val pipeline = Seq(
    unionWith(database.statisticsSubsetFactCount.name),
    unionWith(database.statisticsSubsetNodeCount.name),
    unionWith(database.statisticsSubsetOrphanNodeCount.name),
    unionWith(database.statisticsSubsetRouteCount.name),
    unionWith(database.statisticsSubsetRouteDistance.name),
    unionWith(database.statisticsSubsetFacts.name),
    unionWith(database.statisticsSubsetOrphanRouteCount.name),
    unionWith(database.statisticsSubsetChangeCount.name)
  )

  def execute(): Seq[StatisticValue] = {
    log.debugElapsed {
      val values = database.statisticsSubsetNetworkCount.aggregate[StatisticValue](pipeline)
      (s"${values.size} values", values)
    }
  }
}
