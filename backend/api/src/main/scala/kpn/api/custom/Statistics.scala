package kpn.api.custom

import kpn.api.common.statistics.Statistic

case class Statistics(map: Map[String, Statistic]) {

  def get(key: String): Statistic = map.getOrElse(key, Statistic.empty)

}
