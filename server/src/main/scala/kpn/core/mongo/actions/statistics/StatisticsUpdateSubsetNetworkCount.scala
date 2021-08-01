package kpn.core.mongo.actions.statistics

import kpn.core.mongo.Database
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetNetworkCount.log
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetNetworkCount.pipeline
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log

object StatisticsUpdateSubsetNetworkCount extends MongoQuery {
  private val log = Log(classOf[StatisticsUpdateSubsetNetworkCount])
  private val pipeline = readPipeline("pipeline")
}

class StatisticsUpdateSubsetNetworkCount(database: Database) {
  def execute(): Unit = {
    log.debugElapsed {
      val values = database.networkInfos.aggregate[StatisticValue](pipeline.stages)
      (s"${values.size} values", ())
    }
  }
}
