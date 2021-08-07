package kpn.server.repository

import kpn.api.common.statistics.StatisticValues
import kpn.core.mongo.Database
import kpn.core.mongo.actions.statistics.MongoQueryStatistics
import org.springframework.stereotype.Component

@Component
class StatisticsRepositoryImpl(database: Database) extends StatisticsRepository {

  override def statisticValues(): Seq[StatisticValues] = {
    new MongoQueryStatistics(database).execute()
  }

}
