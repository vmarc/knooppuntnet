package kpn.shared.statistics

object Statistic {

  val empty: Statistic = Statistic(
    "-",
    CountryStatistic("-", "-", "-", "-", "-", "-"),
    CountryStatistic("-", "-", "-", "-", "-", "-"),
    CountryStatistic("-", "-", "-", "-", "-", "-")
  )
}

case class Statistic(
  total: String,
  nl: CountryStatistic,
  be: CountryStatistic,
  de: CountryStatistic
)
