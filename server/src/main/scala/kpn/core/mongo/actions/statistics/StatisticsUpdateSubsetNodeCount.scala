package kpn.core.mongo.actions.statistics

import kpn.core.mongo.Database
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetNodeCount.log
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetNodeCount.pipeline
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log

object StatisticsUpdateSubsetNodeCount extends MongoQuery {
  private val log = Log(classOf[StatisticsUpdateSubsetNodeCount])
  private val pipeline = readPipeline("pipeline")
}

class StatisticsUpdateSubsetNodeCount(database: Database) {
  def execute(): Unit = {
    log.debugElapsed {
      val values = database.nodes.aggregate[StatisticValue](pipeline.stages)
      (s"${values.size} values", ())
    }
  }
}
