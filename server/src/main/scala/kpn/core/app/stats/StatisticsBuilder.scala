package kpn.core.app.stats

import kpn.api.common.statistics.CountryStatistic
import kpn.api.common.statistics.Statistic
import kpn.api.custom.Statistics
import kpn.api.custom.Subset
import kpn.core.util.Formatter.number
import kpn.core.util.Formatter.percentage

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

    def toStatistics: Statistics = Statistics(stats)

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
            percentage(numerator.counts.getOrElse(Subset.nlHiking, 0L), denominator.counts.getOrElse(Subset.nlHiking, 0L)),
            percentage(numerator.counts.getOrElse(Subset.nlBicycle, 0L), denominator.counts.getOrElse(Subset.nlBicycle, 0L)),
            percentage(numerator.counts.getOrElse(Subset.nlHorseRiding, 0L), denominator.counts.getOrElse(Subset.nlHorseRiding, 0L)),
            percentage(numerator.counts.getOrElse(Subset.nlMotorboat, 0L), denominator.counts.getOrElse(Subset.nlMotorboat, 0L)),
            percentage(numerator.counts.getOrElse(Subset.nlCanoe, 0L), denominator.counts.getOrElse(Subset.nlCanoe, 0L)),
            percentage(numerator.counts.getOrElse(Subset.nlInlineSkates, 0L), denominator.counts.getOrElse(Subset.nlInlineSkates, 0L))
          ),
          CountryStatistic(
            percentage(numerator.counts.getOrElse(Subset.beHiking, 0L), denominator.counts.getOrElse(Subset.beHiking, 0L)),
            percentage(numerator.counts.getOrElse(Subset.beBicycle, 0L), denominator.counts.getOrElse(Subset.beBicycle, 0L)),
            percentage(numerator.counts.getOrElse(Subset.beHorseRiding, 0L), denominator.counts.getOrElse(Subset.beHorseRiding, 0L)),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            percentage(numerator.counts.getOrElse(Subset.deHiking, 0L), denominator.counts.getOrElse(Subset.deHiking, 0L)),
            percentage(numerator.counts.getOrElse(Subset.deBicycle, 0L), denominator.counts.getOrElse(Subset.deBicycle, 0L)),
            percentage(numerator.counts.getOrElse(Subset.deHorseRiding, 0L), denominator.counts.getOrElse(Subset.deHorseRiding, 0L)),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            percentage(numerator.counts.getOrElse(Subset.frHiking, 0L), denominator.counts.getOrElse(Subset.frHiking, 0L)),
            percentage(numerator.counts.getOrElse(Subset.frBicycle, 0L), denominator.counts.getOrElse(Subset.frBicycle, 0L)),
            percentage(numerator.counts.getOrElse(Subset.frHorseRiding, 0L), denominator.counts.getOrElse(Subset.frHorseRiding, 0L)),
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
            percentage(numerator.counts.getOrElse(Subset.nlHiking, 0L), denominator.counts.getOrElse(Subset.nlHiking, 0L)),
            percentage(numerator.counts.getOrElse(Subset.nlBicycle, 0L), denominator.counts.getOrElse(Subset.nlBicycle, 0L)),
            percentage(numerator.counts.getOrElse(Subset.nlHorseRiding, 0L), denominator.counts.getOrElse(Subset.nlHorseRiding, 0L)),
            percentage(numerator.counts.getOrElse(Subset.nlMotorboat, 0L), denominator.counts.getOrElse(Subset.nlMotorboat, 0L)),
            percentage(numerator.counts.getOrElse(Subset.nlCanoe, 0L), denominator.counts.getOrElse(Subset.nlCanoe, 0L)),
            percentage(numerator.counts.getOrElse(Subset.nlInlineSkates, 0L), denominator.counts.getOrElse(Subset.nlInlineSkates, 0L))
          ),
          CountryStatistic(
            percentage(numerator.counts.getOrElse(Subset.beHiking, 0L), denominator.counts.getOrElse(Subset.beHiking, 0L)),
            percentage(numerator.counts.getOrElse(Subset.beBicycle, 0L), denominator.counts.getOrElse(Subset.beBicycle, 0L)),
            percentage(numerator.counts.getOrElse(Subset.beHorseRiding, 0L), denominator.counts.getOrElse(Subset.beHorseRiding, 0L)),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            percentage(numerator.counts.getOrElse(Subset.deHiking, 0L), denominator.counts.getOrElse(Subset.deHiking, 0L)),
            percentage(numerator.counts.getOrElse(Subset.deBicycle, 0L), denominator.counts.getOrElse(Subset.deBicycle, 0L)),
            percentage(numerator.counts.getOrElse(Subset.deHorseRiding, 0L), denominator.counts.getOrElse(Subset.deHorseRiding, 0L)),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            percentage(numerator.counts.getOrElse(Subset.frHiking, 0L), denominator.counts.getOrElse(Subset.frHiking, 0L)),
            percentage(numerator.counts.getOrElse(Subset.frBicycle, 0L), denominator.counts.getOrElse(Subset.frBicycle, 0L)),
            percentage(numerator.counts.getOrElse(Subset.frHorseRiding, 0L), denominator.counts.getOrElse(Subset.frHorseRiding, 0L)),
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
            number(count.counts.getOrElse(Subset.nlHiking, 0L) / 1000),
            number(count.counts.getOrElse(Subset.nlBicycle, 0L) / 1000),
            number(count.counts.getOrElse(Subset.nlHorseRiding, 0L) / 1000),
            number(count.counts.getOrElse(Subset.nlMotorboat, 0L) / 1000),
            number(count.counts.getOrElse(Subset.nlCanoe, 0L) / 1000),
            number(count.counts.getOrElse(Subset.nlInlineSkates, 0L) / 1000)
          ),
          CountryStatistic(
            number(count.counts.getOrElse(Subset.beHiking, 0L) / 1000),
            number(count.counts.getOrElse(Subset.beBicycle, 0L) / 1000),
            number(count.counts.getOrElse(Subset.beHorseRiding, 0L) / 1000),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            number(count.counts.getOrElse(Subset.deHiking, 0L) / 1000),
            number(count.counts.getOrElse(Subset.deBicycle, 0L) / 1000),
            number(count.counts.getOrElse(Subset.deHorseRiding, 0L) / 1000),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            number(count.counts.getOrElse(Subset.frHiking, 0L) / 1000),
            number(count.counts.getOrElse(Subset.frBicycle, 0L) / 1000),
            number(count.counts.getOrElse(Subset.frHorseRiding, 0L) / 1000),
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
            number(count.counts.getOrElse(Subset.nlHiking, 0L) / 1000),
            number(count.counts.getOrElse(Subset.nlBicycle, 0L) / 1000),
            number(count.counts.getOrElse(Subset.nlHorseRiding, 0L) / 1000),
            number(count.counts.getOrElse(Subset.nlMotorboat, 0L) / 1000),
            number(count.counts.getOrElse(Subset.nlCanoe, 0L) / 1000),
            number(count.counts.getOrElse(Subset.nlInlineSkates, 0L) / 1000)
          ),
          CountryStatistic(
            number(count.counts.getOrElse(Subset.beHiking, 0L) / 1000),
            number(count.counts.getOrElse(Subset.beBicycle, 0L) / 1000),
            number(count.counts.getOrElse(Subset.beHorseRiding, 0L) / 1000),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            number(count.counts.getOrElse(Subset.deHiking, 0L) / 1000),
            number(count.counts.getOrElse(Subset.deBicycle, 0L) / 1000),
            number(count.counts.getOrElse(Subset.deHorseRiding, 0L) / 1000),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            number(count.counts.getOrElse(Subset.frHiking, 0L) / 1000),
            number(count.counts.getOrElse(Subset.frBicycle, 0L) / 1000),
            number(count.counts.getOrElse(Subset.frHorseRiding, 0L) / 1000),
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
            percentage(count.counts.getOrElse(Subset.nlHiking, 0L) - failed.counts.getOrElse(Subset.nlHiking, 0L), count.counts.getOrElse(Subset.nlHiking, 0L)),
            percentage(count.counts.getOrElse(Subset.nlBicycle, 0L) - failed.counts.getOrElse(Subset.nlBicycle, 0L), count.counts.getOrElse(Subset.nlBicycle, 0L)),
            percentage(count.counts.getOrElse(Subset.nlHorseRiding, 0L) - failed.counts.getOrElse(Subset.nlHorseRiding, 0L), count.counts.getOrElse(Subset
              .nlHorseRiding, 0L)),
            percentage(count.counts.getOrElse(Subset.nlMotorboat, 0L) - failed.counts.getOrElse(Subset.nlMotorboat, 0L), count.counts.getOrElse(Subset.nlMotorboat, 0L)),
            percentage(count.counts.getOrElse(Subset.nlCanoe, 0L) - failed.counts.getOrElse(Subset.nlCanoe, 0L), count.counts.getOrElse(Subset.nlCanoe, 0L)),
            percentage(count.counts.getOrElse(Subset.nlInlineSkates, 0L) - failed.counts.getOrElse(Subset.nlInlineSkates, 0L), count.counts.getOrElse(Subset
              .nlInlineSkates, 0L))
          ),
          CountryStatistic(
            percentage(count.counts.getOrElse(Subset.beHiking, 0L) - failed.counts.getOrElse(Subset.beHiking, 0L), count.counts.getOrElse(Subset.beHiking, 0L)),
            percentage(count.counts.getOrElse(Subset.beBicycle, 0L) - failed.counts.getOrElse(Subset.beBicycle, 0L), count.counts.getOrElse(Subset.beBicycle, 0L)),
            percentage(count.counts.getOrElse(Subset.beHorseRiding, 0L) - failed.counts.getOrElse(Subset.beHorseRiding, 0L), count.counts.getOrElse(Subset.beHorseRiding,
              0L)),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            percentage(count.counts.getOrElse(Subset.deHiking, 0L) - failed.counts.getOrElse(Subset.deHiking, 0L), count.counts.getOrElse(Subset.deHiking, 0L)),
            percentage(count.counts.getOrElse(Subset.deBicycle, 0L) - failed.counts.getOrElse(Subset.deBicycle, 0L), count.counts.getOrElse(Subset.deBicycle, 0L)),
            percentage(count.counts.getOrElse(Subset.deHorseRiding, 0L) - failed.counts.getOrElse(Subset.deHorseRiding, 0L), count.counts.getOrElse(Subset
              .deHorseRiding, 0L)),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            percentage(count.counts.getOrElse(Subset.frHiking, 0L) - failed.counts.getOrElse(Subset.frHiking, 0L), count.counts.getOrElse(Subset.frHiking, 0L)),
            percentage(count.counts.getOrElse(Subset.frBicycle, 0L) - failed.counts.getOrElse(Subset.frBicycle, 0L), count.counts.getOrElse(Subset.frBicycle, 0L)),
            percentage(count.counts.getOrElse(Subset.frHorseRiding, 0L) - failed.counts.getOrElse(Subset.frHorseRiding, 0L), count.counts.getOrElse(Subset
              .frHorseRiding, 0L)),
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
            percentage(checkCount.counts.getOrElse(Subset.nlHiking, 0L), nodeCount.counts.getOrElse(Subset.nlHiking, 0L)),
            percentage(checkCount.counts.getOrElse(Subset.nlBicycle, 0L), nodeCount.counts.getOrElse(Subset.nlBicycle, 0L)),
            percentage(checkCount.counts.getOrElse(Subset.nlHorseRiding, 0L), nodeCount.counts.getOrElse(Subset.nlHorseRiding, 0L)),
            percentage(checkCount.counts.getOrElse(Subset.nlMotorboat, 0L), nodeCount.counts.getOrElse(Subset.nlMotorboat, 0L)),
            percentage(checkCount.counts.getOrElse(Subset.nlCanoe, 0L), nodeCount.counts.getOrElse(Subset.nlCanoe, 0L)),
            percentage(checkCount.counts.getOrElse(Subset.nlInlineSkates, 0L), nodeCount.counts.getOrElse(Subset.nlInlineSkates, 0L))
          ),
          CountryStatistic(
            percentage(checkCount.counts.getOrElse(Subset.beHiking, 0L), nodeCount.counts.getOrElse(Subset.beHiking, 0L)),
            percentage(checkCount.counts.getOrElse(Subset.beBicycle, 0L), nodeCount.counts.getOrElse(Subset.beBicycle, 0L)),
            percentage(checkCount.counts.getOrElse(Subset.beHorseRiding, 0L), nodeCount.counts.getOrElse(Subset.beHorseRiding, 0L)),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            percentage(checkCount.counts.getOrElse(Subset.deHiking, 0L), nodeCount.counts.getOrElse(Subset.deHiking, 0L)),
            percentage(checkCount.counts.getOrElse(Subset.deBicycle, 0L), nodeCount.counts.getOrElse(Subset.deBicycle, 0L)),
            percentage(checkCount.counts.getOrElse(Subset.deHorseRiding, 0L), nodeCount.counts.getOrElse(Subset.deHorseRiding, 0L)),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            percentage(checkCount.counts.getOrElse(Subset.frHiking, 0L), nodeCount.counts.getOrElse(Subset.frHiking, 0L)),
            percentage(checkCount.counts.getOrElse(Subset.frBicycle, 0L), nodeCount.counts.getOrElse(Subset.frBicycle, 0L)),
            percentage(checkCount.counts.getOrElse(Subset.frHorseRiding, 0L), nodeCount.counts.getOrElse(Subset.frHorseRiding, 0L)),
            "-",
            "-",
            "-"
          )
        )
      }
    }
  }

}
