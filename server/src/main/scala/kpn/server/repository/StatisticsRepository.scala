package kpn.server.repository

import kpn.database.actions.statistics.MongoQueryStatistics
import kpn.database.actions.statistics.StatisticLongValues
import kpn.database.base.Database
import org.springframework.stereotype.Component

@Component
class StatisticsRepository(database: Database) {

  def statisticValues(): Seq[StatisticLongValues] = {
    new MongoQueryStatistics(database).execute()
  }
}
