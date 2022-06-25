package kpn.server.repository

import kpn.database.actions.statistics.StatisticLongValues

trait StatisticsRepository {

  def statisticValues(): Seq[StatisticLongValues]

}
