package kpn.core.app.stats

import kpn.api.common.statistics.CountryStatistic
import kpn.api.common.statistics.Statistic
import kpn.api.custom.Subset
import kpn.core.util.Formatter.number

case class Figure(
  name: String,
  total: Int,
  counts: Map[Subset, Int]
) {
  def toStatistic: Statistic = {
    Statistic(
      number(total),
      CountryStatistic(
        number(counts.getOrElse(Subset.nlHiking, 0)),
        number(counts.getOrElse(Subset.nlBicycle, 0)),
        number(counts.getOrElse(Subset.nlHorseRiding, 0)),
        number(counts.getOrElse(Subset.nlMotorboat, 0)),
        number(counts.getOrElse(Subset.nlCanoe, 0)),
        number(counts.getOrElse(Subset.nlInlineSkates, 0))
      ),
      CountryStatistic(
        number(counts.getOrElse(Subset.beHiking, 0)),
        number(counts.getOrElse(Subset.beBicycle, 0)),
        number(counts.getOrElse(Subset.beHorseRiding, 0)),
        "-",
        "-",
        "-"
      ),
      CountryStatistic(
        number(counts.getOrElse(Subset.deHiking, 0)),
        number(counts.getOrElse(Subset.deBicycle, 0)),
        number(counts.getOrElse(Subset.deHorseRiding, 0)),
        "-",
        "-",
        "-"
      ),
      CountryStatistic(
        number(counts.getOrElse(Subset.frHiking, 0)),
        number(counts.getOrElse(Subset.frBicycle, 0)),
        number(counts.getOrElse(Subset.frHorseRiding, 0)),
        "-",
        "-",
        "-"
      )
    )
  }

}