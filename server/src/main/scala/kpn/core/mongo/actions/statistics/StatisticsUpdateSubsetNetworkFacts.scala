package kpn.core.mongo.actions.statistics

import kpn.core.mongo.Database
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetNetworkFacts.log
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetNetworkFacts.pipeline
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log

object StatisticsUpdateSubsetNetworkFacts extends MongoQuery {
  private val log = Log(classOf[StatisticsUpdateSubsetNetworkFacts])
  private val pipeline = readPipeline("pipeline")
}

class StatisticsUpdateSubsetNetworkFacts(database: Database) {
  def execute(): Unit = {
    log.debugElapsed {
      val values = database.networkInfos.aggregate[StatisticValue](pipeline.stages)
      (s"${values.size} values", ())
    }
  }
}
