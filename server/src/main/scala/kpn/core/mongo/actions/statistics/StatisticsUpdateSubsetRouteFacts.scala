package kpn.core.mongo.actions.statistics

import kpn.core.mongo.Database
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetRouteFacts.log
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetRouteFacts.pipeline
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log

object StatisticsUpdateSubsetRouteFacts extends MongoQuery {
  private val log = Log(classOf[StatisticsUpdateSubsetRouteFacts])
  private val pipeline = readPipeline("pipeline")
}

class StatisticsUpdateSubsetRouteFacts(database: Database) {
  def execute(): Unit = {
    log.debugElapsed {
      val values = database.routes.aggregate[StatisticValue](pipeline.stages)
      (s"${values.size} values", ())
    }
  }
}
