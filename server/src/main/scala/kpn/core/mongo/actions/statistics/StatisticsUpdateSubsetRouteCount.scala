package kpn.core.mongo.actions.statistics

import kpn.core.mongo.Database
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetRouteCount.log
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetRouteCount.pipeline
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log

object StatisticsUpdateSubsetRouteCount extends MongoQuery {
  private val log = Log(classOf[StatisticsUpdateSubsetRouteCount])
  private val pipeline = readPipeline("pipeline")
}

class StatisticsUpdateSubsetRouteCount(database: Database) {
  def execute(): Unit = {
    log.debugElapsed {
      val values = database.routes.aggregate[StatisticValue](pipeline.stages)
      (s"${values.size} values", ())
    }
  }
}
