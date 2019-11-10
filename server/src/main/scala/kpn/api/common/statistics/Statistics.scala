package kpn.api.common.statistics

case class Statistics(map: Map[String, Statistic]) {

  def get(key: String): Statistic = map.getOrElse(key, Statistic.empty)

}
