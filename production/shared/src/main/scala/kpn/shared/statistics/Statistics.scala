package kpn.shared.statistics

import kpn.shared.Subset

case class Statistics(map: Map[String, Statistic]) {

  def get(key: String): Statistic = map.getOrElse(key, Statistic.empty)

  def figure(subset: Subset, key: String): String = {
    val statistic = get(key)
    subset match {
      case Subset.beHiking => statistic.be.rwn
      case Subset.nlHiking => statistic.nl.rwn
      case Subset.deHiking => statistic.de.rwn
      case Subset.beBicycle => statistic.be.rcn
      case Subset.nlBicycle => statistic.nl.rcn
      case Subset.deBicycle => statistic.de.rcn
    }
  }

  def figure(key: String): String = {
    get(key).total
  }
}
