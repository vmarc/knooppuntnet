package kpn.core.tools.metrics

import kpn.database.base.MetricsDatabaseImpl
import kpn.database.util.Mongo
import kpn.server.repository.MetricsRepository
import kpn.server.repository.MetricsRepositoryImpl

object DayReportTool {
  def main(args: Array[String]): Unit = {
    val mongoClient = Mongo.client
    try {
      val database = new MetricsDatabaseImpl(mongoClient.getDatabase("kpn-metrics").withCodecRegistry(Mongo.codecRegistry))
      val metricsRepository = new MetricsRepositoryImpl(database)
      new DayReportTool(metricsRepository).report()
    }
    finally {
      mongoClient.close()
    }
  }
}

class DayReportTool(metricsRepository: MetricsRepository) {
  def report(): Unit = {
    metricsRepository.apiActionsDay(2023, 4, 1).foreach { action =>
      println(s"${action.timestamp.toHuman} ${action.deviceName} ${action.user} ${action.name}  ${action.args}")
    }
  }
}
