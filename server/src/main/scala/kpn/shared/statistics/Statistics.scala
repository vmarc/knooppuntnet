package kpn.shared.statistics

case class Statistics(map: Map[String, Statistic]) {

  def get(key: String): Statistic = map.getOrElse(key, Statistic.empty)

}
