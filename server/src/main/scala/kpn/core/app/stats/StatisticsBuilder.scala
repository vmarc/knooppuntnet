package kpn.core.app.stats

import kpn.core.util.Formatter.number
import kpn.core.util.Formatter.percentage
import kpn.shared.Subset
import kpn.shared.statistics.CountryStatistic
import kpn.shared.statistics.Statistic
import kpn.shared.statistics.Statistics

object StatisticsBuilder {

  def build(figures: Map[String, Figure]): Statistics = {
    new StatisticsBuilder(figures).toStatistics
  }

  private class StatisticsBuilder(figures: Map[String, Figure]) {

    private val fromdb = figures.map { case (key, figure) => key -> figure.toStatistic }

    private val extra = Map(
      "km" -> km,
      "OrphanRouteKm" -> orphanRouteKm,
      "RouteBrokenPercentage" -> routesBrokenPercentage,
      "RouteNotContiniousPercentage" -> routesNotContiniousPercentage,
      "IntegrityCheckPassRate" -> integrityCheckPassRate,
      "IntegrityCheckCoverage" -> integrityCheckCoverage
    )

    private val stats: Map[String, Statistic] = fromdb ++ extra

    def toStatistics = Statistics(stats)

    private def routesBrokenPercentage = {
      if (!(figures.contains("RouteBrokenCount") && figures.contains("RouteCount"))) {
        Statistic.empty
      }
      else {
        val numerator = figures("RouteBrokenCount")
        val denominator = figures("RouteCount")
        new Statistic(
          percentage(numerator.total, denominator.total),
          CountryStatistic(
            percentage(numerator.counts(Subset.nlHiking), denominator.counts(Subset.nlHiking)),
            percentage(numerator.counts(Subset.nlBicycle), denominator.counts(Subset.nlBicycle)),
            percentage(numerator.counts(Subset.nlHorseRiding), denominator.counts(Subset.nlHorseRiding)),
            percentage(numerator.counts(Subset.nlMotorboat), denominator.counts(Subset.nlMotorboat)),
            percentage(numerator.counts(Subset.nlCanoe), denominator.counts(Subset.nlCanoe)),
            percentage(numerator.counts(Subset.nlInlineSkates), denominator.counts(Subset.nlInlineSkates))
          ),
          CountryStatistic(
            percentage(numerator.counts(Subset.beHiking), denominator.counts(Subset.beHiking)),
            percentage(numerator.counts(Subset.beBicycle), denominator.counts(Subset.beBicycle)),
            percentage(numerator.counts(Subset.beHorseRiding), denominator.counts(Subset.beHorseRiding)),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            percentage(numerator.counts(Subset.deHiking), denominator.counts(Subset.deHiking)),
            percentage(numerator.counts(Subset.deBicycle), denominator.counts(Subset.deBicycle)),
            percentage(numerator.counts(Subset.deHorseRiding), denominator.counts(Subset.deHorseRiding)),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            percentage(numerator.counts(Subset.frHiking), denominator.counts(Subset.frHiking)),
            percentage(numerator.counts(Subset.frBicycle), denominator.counts(Subset.frBicycle)),
            percentage(numerator.counts(Subset.frHorseRiding), denominator.counts(Subset.frHorseRiding)),
            "-",
            "-",
            "-"
          )
        )
      }
    }

    private def routesNotContiniousPercentage = {
      if (!(figures.contains("RouteNotContiniousCount") && figures.contains("RouteCount"))) {
        Statistic.empty
      }
      else {
        val numerator = figures("RouteNotContiniousCount")
        val denominator = figures("RouteCount")
        new Statistic(
          percentage(numerator.total, denominator.total),
          CountryStatistic(
            percentage(numerator.counts(Subset.nlHiking), denominator.counts(Subset.nlHiking)),
            percentage(numerator.counts(Subset.nlBicycle), denominator.counts(Subset.nlBicycle)),
            percentage(numerator.counts(Subset.nlHorseRiding), denominator.counts(Subset.nlHorseRiding)),
            percentage(numerator.counts(Subset.nlMotorboat), denominator.counts(Subset.nlMotorboat)),
            percentage(numerator.counts(Subset.nlCanoe), denominator.counts(Subset.nlCanoe)),
            percentage(numerator.counts(Subset.nlInlineSkates), denominator.counts(Subset.nlInlineSkates))
          ),
          CountryStatistic(
            percentage(numerator.counts(Subset.beHiking), denominator.counts(Subset.beHiking)),
            percentage(numerator.counts(Subset.beBicycle), denominator.counts(Subset.beBicycle)),
            percentage(numerator.counts(Subset.beHorseRiding), denominator.counts(Subset.beHorseRiding)),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            percentage(numerator.counts(Subset.deHiking), denominator.counts(Subset.deHiking)),
            percentage(numerator.counts(Subset.deBicycle), denominator.counts(Subset.deBicycle)),
            percentage(numerator.counts(Subset.deHorseRiding), denominator.counts(Subset.deHorseRiding)),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            percentage(numerator.counts(Subset.frHiking), denominator.counts(Subset.frHiking)),
            percentage(numerator.counts(Subset.frBicycle), denominator.counts(Subset.frBicycle)),
            percentage(numerator.counts(Subset.frHorseRiding), denominator.counts(Subset.frHorseRiding)),
            "-",
            "-",
            "-"
          )
        )
      }
    }

    private def km = {
      if (!figures.contains("MeterCount")) {
        Statistic.empty
      }
      else {
        val count = figures("MeterCount")
        new Statistic(
          number(count.total / 1000),
          CountryStatistic(
            number(count.counts(Subset.nlHiking) / 1000),
            number(count.counts(Subset.nlBicycle) / 1000),
            number(count.counts(Subset.nlHorseRiding) / 1000),
            number(count.counts(Subset.nlMotorboat) / 1000),
            number(count.counts(Subset.nlCanoe) / 1000),
            number(count.counts(Subset.nlInlineSkates) / 1000)
          ),
          CountryStatistic(
            number(count.counts(Subset.beHiking) / 1000),
            number(count.counts(Subset.beBicycle) / 1000),
            number(count.counts(Subset.beHorseRiding) / 1000),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            number(count.counts(Subset.deHiking) / 1000),
            number(count.counts(Subset.deBicycle) / 1000),
            number(count.counts(Subset.deHorseRiding) / 1000),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            number(count.counts(Subset.frHiking) / 1000),
            number(count.counts(Subset.frBicycle) / 1000),
            number(count.counts(Subset.frHorseRiding) / 1000),
            "-",
            "-",
            "-"
          )
        )
      }
    }

    private def orphanRouteKm = {
      if (!figures.contains("OrphanRouteMeterCount")) {
        Statistic.empty
      }
      else {
        val count = figures("OrphanRouteMeterCount")
        new Statistic(
          number(count.total / 1000),
          CountryStatistic(
            number(count.counts(Subset.nlHiking) / 1000),
            number(count.counts(Subset.nlBicycle) / 1000),
            number(count.counts(Subset.nlHorseRiding) / 1000),
            number(count.counts(Subset.nlMotorboat) / 1000),
            number(count.counts(Subset.nlCanoe) / 1000),
            number(count.counts(Subset.nlInlineSkates) / 1000)
          ),
          CountryStatistic(
            number(count.counts(Subset.beHiking) / 1000),
            number(count.counts(Subset.beBicycle) / 1000),
            number(count.counts(Subset.beHorseRiding) / 1000),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            number(count.counts(Subset.deHiking) / 1000),
            number(count.counts(Subset.deBicycle) / 1000),
            number(count.counts(Subset.deHorseRiding) / 1000),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            number(count.counts(Subset.frHiking) / 1000),
            number(count.counts(Subset.frBicycle) / 1000),
            number(count.counts(Subset.frHorseRiding) / 1000),
            "-",
            "-",
            "-"
          )
        )
      }
    }

    private def integrityCheckPassRate = {
      if (!(figures.contains("IntegrityCheckFailedCount") && figures.contains("IntegrityCheckCount"))) {
        Statistic.empty
      }
      else {
        val failed = figures("IntegrityCheckFailedCount")
        val count = figures("IntegrityCheckCount")
        new Statistic(
          percentage(count.total - failed.total, count.total),
          CountryStatistic(
            percentage(count.counts(Subset.nlHiking) - failed.counts(Subset.nlHiking), count.counts(Subset.nlHiking)),
            percentage(count.counts(Subset.nlBicycle) - failed.counts(Subset.nlBicycle), count.counts(Subset.nlBicycle)),
            percentage(count.counts(Subset.nlHorseRiding) - failed.counts(Subset.nlHorseRiding), count.counts(Subset.nlHorseRiding)),
            percentage(count.counts(Subset.nlMotorboat) - failed.counts(Subset.nlMotorboat), count.counts(Subset.nlMotorboat)),
            percentage(count.counts(Subset.nlCanoe) - failed.counts(Subset.nlCanoe), count.counts(Subset.nlCanoe)),
            percentage(count.counts(Subset.nlInlineSkates) - failed.counts(Subset.nlInlineSkates), count.counts(Subset.nlInlineSkates))
          ),
          CountryStatistic(
            percentage(count.counts(Subset.beHiking) - failed.counts(Subset.beHiking), count.counts(Subset.beHiking)),
            percentage(count.counts(Subset.beBicycle) - failed.counts(Subset.beBicycle), count.counts(Subset.beBicycle)),
            percentage(count.counts(Subset.beHorseRiding) - failed.counts(Subset.beHorseRiding), count.counts(Subset.beHorseRiding)),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            percentage(count.counts(Subset.deHiking) - failed.counts(Subset.deHiking), count.counts(Subset.deHiking)),
            percentage(count.counts(Subset.deBicycle) - failed.counts(Subset.deBicycle), count.counts(Subset.deBicycle)),
            percentage(count.counts(Subset.deHorseRiding) - failed.counts(Subset.deHorseRiding), count.counts(Subset.deHorseRiding)),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            percentage(count.counts(Subset.frHiking) - failed.counts(Subset.frHiking), count.counts(Subset.frHiking)),
            percentage(count.counts(Subset.frBicycle) - failed.counts(Subset.frBicycle), count.counts(Subset.frBicycle)),
            percentage(count.counts(Subset.frHorseRiding) - failed.counts(Subset.frHorseRiding), count.counts(Subset.frHorseRiding)),
            "-",
            "-",
            "-"
          )
        )
      }
    }

    private def integrityCheckCoverage = {
      if (!(figures.contains("IntegrityCheckCount") && figures.contains("NodeCount"))) {
        Statistic.empty
      }
      else {
        val checkCount = figures("IntegrityCheckCount")
        val nodeCount = figures("NodeCount")
        new Statistic(
          percentage(checkCount.total, nodeCount.total),
          CountryStatistic(
            percentage(checkCount.counts(Subset.nlHiking), nodeCount.counts(Subset.nlHiking)),
            percentage(checkCount.counts(Subset.nlBicycle), nodeCount.counts(Subset.nlBicycle)),
            percentage(checkCount.counts(Subset.nlHorseRiding), nodeCount.counts(Subset.nlHorseRiding)),
            percentage(checkCount.counts(Subset.nlMotorboat), nodeCount.counts(Subset.nlMotorboat)),
            percentage(checkCount.counts(Subset.nlCanoe), nodeCount.counts(Subset.nlCanoe)),
            percentage(checkCount.counts(Subset.nlInlineSkates), nodeCount.counts(Subset.nlInlineSkates))
          ),
          CountryStatistic(
            percentage(checkCount.counts(Subset.beHiking), nodeCount.counts(Subset.beHiking)),
            percentage(checkCount.counts(Subset.beBicycle), nodeCount.counts(Subset.beBicycle)),
            percentage(checkCount.counts(Subset.beHorseRiding), nodeCount.counts(Subset.beHorseRiding)),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            percentage(checkCount.counts(Subset.deHiking), nodeCount.counts(Subset.deHiking)),
            percentage(checkCount.counts(Subset.deBicycle), nodeCount.counts(Subset.deBicycle)),
            percentage(checkCount.counts(Subset.deHorseRiding), nodeCount.counts(Subset.deHorseRiding)),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            percentage(checkCount.counts(Subset.frHiking), nodeCount.counts(Subset.frHiking)),
            percentage(checkCount.counts(Subset.frBicycle), nodeCount.counts(Subset.frBicycle)),
            percentage(checkCount.counts(Subset.frHorseRiding), nodeCount.counts(Subset.frHorseRiding)),
            "-",
            "-",
            "-"
          )
        )
      }
    }
  }

}
