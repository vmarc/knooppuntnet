package kpn.server.repository

import kpn.api.common.statistics.StatisticValues
import kpn.database.actions.statistics.MongoQueryStatistics
import kpn.database.base.Database
import org.springframework.stereotype.Component

@Component
class StatisticsRepositoryImpl(database: Database) extends StatisticsRepository {

  override def statisticValues(): Seq[StatisticValues] = {
    new MongoQueryStatistics(database).execute()
  }

}
