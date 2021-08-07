package kpn.server.repository

import kpn.api.common.statistics.StatisticValues

trait StatisticsRepository {

  def statisticValues(): Seq[StatisticValues]

}
