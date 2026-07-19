package kpn.api.common.statistics;

import kpn.api.common.statistics.CountryStatistic;

public record Statistic(
  String total,
  CountryStatistic nl,
  CountryStatistic be,
  CountryStatistic de,
  CountryStatistic fr,
  CountryStatistic at,
  CountryStatistic es
) {
}

/*
package kpn.api.common.statistics

object Statistic {

  val empty: Statistic = Statistic(
    "-",
    CountryStatistic("-", "-", "-", "-", "-", "-"),
    CountryStatistic("-", "-", "-", "-", "-", "-"),
    CountryStatistic("-", "-", "-", "-", "-", "-"),
    CountryStatistic("-", "-", "-", "-", "-", "-"),
    CountryStatistic("-", "-", "-", "-", "-", "-"),
    CountryStatistic("-", "-", "-", "-", "-", "-")
  )
}

case class Statistic(
  total: String,
  nl: CountryStatistic,
  be: CountryStatistic,
  de: CountryStatistic,
  fr: CountryStatistic,
  at: CountryStatistic,
  es: CountryStatistic
)

*/
