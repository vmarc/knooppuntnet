package kpn.core.mongo.actions.statistics

import kpn.core.mongo.Database
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetChangeCount.log
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetChangeCount.pipeline
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log

object StatisticsUpdateSubsetChangeCount extends MongoQuery {
  private val log = Log(classOf[StatisticsUpdateSubsetChangeCount])
  private val pipeline = readPipeline("pipeline")
}

class StatisticsUpdateSubsetChangeCount(database: Database) {
  def execute(): Unit = {
    log.debugElapsed {
      val values = database.changeSetSummaries.aggregate[StatisticValue](pipeline.stages)
      (s"${values.size} values", ())
    }
  }
}
