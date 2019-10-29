package kpn.core.app.stats

import kpn.core.util.Formatter.number
import kpn.shared.Subset
import kpn.shared.statistics.CountryStatistic
import kpn.shared.statistics.Statistic

case class Figure(
  name: String,
  total: Int,
  counts: Map[Subset, Int]
) {
  def toStatistic: Statistic = {
    Statistic(
      number(total),
      CountryStatistic(
        number(counts(Subset.nlHiking)),
        number(counts(Subset.nlBicycle)),
        number(counts(Subset.nlHorseRiding)),
        number(counts(Subset.nlMotorboat)),
        number(counts(Subset.nlCanoe)),
        number(counts(Subset.nlInlineSkates))
      ),
      CountryStatistic(
        number(counts(Subset.beHiking)),
        number(counts(Subset.beBicycle)),
        number(counts(Subset.beHorseRiding)),
        "-",
        "-",
        "-"
      ),
      CountryStatistic(
        number(counts(Subset.deHiking)),
        number(counts(Subset.deBicycle)),
        number(counts(Subset.deHorseRiding)),
        "-",
        "-",
        "-"
      ),
      CountryStatistic(
        number(counts(Subset.frHiking)),
        number(counts(Subset.frBicycle)),
        number(counts(Subset.frHorseRiding)),
        "-",
        "-",
        "-"
      )
    )
  }

}