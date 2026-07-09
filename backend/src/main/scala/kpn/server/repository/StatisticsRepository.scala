package kpn.server.repository

import kpn.database.actions.statistics.MongoQueryStatistics
import kpn.database.actions.statistics.StatisticLongValues
import kpn.database.base.Database
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
class StatisticsRepository(database: Database) {

  def statisticValues(): Seq[StatisticLongValues] = {
    new MongoQueryStatistics(database).execute()
  }
}
