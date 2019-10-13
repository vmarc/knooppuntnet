package kpn.server.analyzer.engine.analysis.country

import kpn.shared.Country

object CountryCounts {
  def apply(countries: Seq[Option[Country]]): CountryCounts = {
    CountryCounts(countries.groupBy(identity).map(a => a._1 -> a._2.size))
  }
}

case class CountryCounts(counts: Map[Option[Country], Int] = Map.empty) {

  def + (other: CountryCounts): CountryCounts = {
    val keys = counts.keySet ++ other.counts.keySet
    val sums = keys.map { key =>
      val sum = counts.getOrElse(key, 0) + other.counts.getOrElse(key, 0)
      key -> sum
    }
    CountryCounts(sums.toMap)
  }

  def count(country: Country): Int = counts.getOrElse(Some(country), 0)

  def country: Option[Country] = {
    if (counts.isEmpty) {
      None
    }
    else {
      counts.maxBy(_._2)._1
    }
  }

  def asString: String = {
    country match {
      case None => "Foreign country"
      case Some(country) =>
        if (counts.size == 1) {
          "Country=" + country.domain
        }
        else {
          "Country=" + country.domain + " " +
          counts.map{ case (countryOption, count) =>
            val key = countryOption match {
              case None => "Foreign"
              case Some(c) => c.domain
            }
            key + "=" + count
          }.mkString(", ")
        }
    }
  }
}
