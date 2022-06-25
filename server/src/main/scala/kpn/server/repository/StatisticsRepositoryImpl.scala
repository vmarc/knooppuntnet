package kpn.server.repository

import kpn.database.actions.statistics.MongoQueryStatistics
import kpn.database.actions.statistics.StatisticLongValues
import kpn.database.base.Database
import org.springframework.stereotype.Component

@Component
class StatisticsRepositoryImpl(database: Database) extends StatisticsRepository {

  override def statisticValues(): Seq[StatisticLongValues] = {
    new MongoQueryStatistics(database).execute()
  }

}
