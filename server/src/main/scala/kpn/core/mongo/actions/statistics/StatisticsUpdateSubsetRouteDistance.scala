package kpn.core.mongo.actions.statistics

import kpn.core.mongo.Database
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetRouteDistance.log
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetRouteDistance.pipeline
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log

object StatisticsUpdateSubsetRouteDistance extends MongoQuery {
  private val log = Log(classOf[StatisticsUpdateSubsetRouteDistance])
  private val pipeline = readPipeline("pipeline")
}

class StatisticsUpdateSubsetRouteDistance(database: Database) {
  def execute(): Unit = {
    log.debugElapsed {
      val values = database.routes.aggregate[StatisticValue](pipeline.stages)
      (s"${values.size} values", ())
    }
  }
}
