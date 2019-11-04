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
            percentage(numerator.counts.getOrElse(Subset.nlHiking, 0), denominator.counts.getOrElse(Subset.nlHiking, 0)),
            percentage(numerator.counts.getOrElse(Subset.nlBicycle, 0), denominator.counts.getOrElse(Subset.nlBicycle, 0)),
            percentage(numerator.counts.getOrElse(Subset.nlHorseRiding, 0), denominator.counts.getOrElse(Subset.nlHorseRiding, 0)),
            percentage(numerator.counts.getOrElse(Subset.nlMotorboat, 0), denominator.counts.getOrElse(Subset.nlMotorboat, 0)),
            percentage(numerator.counts.getOrElse(Subset.nlCanoe, 0), denominator.counts.getOrElse(Subset.nlCanoe, 0)),
            percentage(numerator.counts.getOrElse(Subset.nlInlineSkates, 0), denominator.counts.getOrElse(Subset.nlInlineSkates, 0))
          ),
          CountryStatistic(
            percentage(numerator.counts.getOrElse(Subset.beHiking, 0), denominator.counts.getOrElse(Subset.beHiking, 0)),
            percentage(numerator.counts.getOrElse(Subset.beBicycle, 0), denominator.counts.getOrElse(Subset.beBicycle, 0)),
            percentage(numerator.counts.getOrElse(Subset.beHorseRiding, 0), denominator.counts.getOrElse(Subset.beHorseRiding, 0)),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            percentage(numerator.counts.getOrElse(Subset.deHiking, 0), denominator.counts.getOrElse(Subset.deHiking, 0)),
            percentage(numerator.counts.getOrElse(Subset.deBicycle, 0), denominator.counts.getOrElse(Subset.deBicycle, 0)),
            percentage(numerator.counts.getOrElse(Subset.deHorseRiding, 0), denominator.counts.getOrElse(Subset.deHorseRiding, 0)),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            percentage(numerator.counts.getOrElse(Subset.frHiking, 0), denominator.counts.getOrElse(Subset.frHiking, 0)),
            percentage(numerator.counts.getOrElse(Subset.frBicycle, 0), denominator.counts.getOrElse(Subset.frBicycle, 0)),
            percentage(numerator.counts.getOrElse(Subset.frHorseRiding, 0), denominator.counts.getOrElse(Subset.frHorseRiding, 0)),
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
            percentage(numerator.counts.getOrElse(Subset.nlHiking, 0), denominator.counts.getOrElse(Subset.nlHiking, 0)),
            percentage(numerator.counts.getOrElse(Subset.nlBicycle, 0), denominator.counts.getOrElse(Subset.nlBicycle, 0)),
            percentage(numerator.counts.getOrElse(Subset.nlHorseRiding, 0), denominator.counts.getOrElse(Subset.nlHorseRiding, 0)),
            percentage(numerator.counts.getOrElse(Subset.nlMotorboat, 0), denominator.counts.getOrElse(Subset.nlMotorboat, 0)),
            percentage(numerator.counts.getOrElse(Subset.nlCanoe, 0), denominator.counts.getOrElse(Subset.nlCanoe, 0)),
            percentage(numerator.counts.getOrElse(Subset.nlInlineSkates, 0), denominator.counts.getOrElse(Subset.nlInlineSkates, 0))
          ),
          CountryStatistic(
            percentage(numerator.counts.getOrElse(Subset.beHiking, 0), denominator.counts.getOrElse(Subset.beHiking, 0)),
            percentage(numerator.counts.getOrElse(Subset.beBicycle, 0), denominator.counts.getOrElse(Subset.beBicycle, 0)),
            percentage(numerator.counts.getOrElse(Subset.beHorseRiding, 0), denominator.counts.getOrElse(Subset.beHorseRiding, 0)),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            percentage(numerator.counts.getOrElse(Subset.deHiking, 0), denominator.counts.getOrElse(Subset.deHiking, 0)),
            percentage(numerator.counts.getOrElse(Subset.deBicycle, 0), denominator.counts.getOrElse(Subset.deBicycle, 0)),
            percentage(numerator.counts.getOrElse(Subset.deHorseRiding, 0), denominator.counts.getOrElse(Subset.deHorseRiding, 0)),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            percentage(numerator.counts.getOrElse(Subset.frHiking, 0), denominator.counts.getOrElse(Subset.frHiking, 0)),
            percentage(numerator.counts.getOrElse(Subset.frBicycle, 0), denominator.counts.getOrElse(Subset.frBicycle, 0)),
            percentage(numerator.counts.getOrElse(Subset.frHorseRiding, 0), denominator.counts.getOrElse(Subset.frHorseRiding, 0)),
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
            number(count.counts.getOrElse(Subset.nlHiking, 0) / 1000),
            number(count.counts.getOrElse(Subset.nlBicycle, 0) / 1000),
            number(count.counts.getOrElse(Subset.nlHorseRiding, 0) / 1000),
            number(count.counts.getOrElse(Subset.nlMotorboat, 0) / 1000),
            number(count.counts.getOrElse(Subset.nlCanoe, 0) / 1000),
            number(count.counts.getOrElse(Subset.nlInlineSkates, 0) / 1000)
          ),
          CountryStatistic(
            number(count.counts.getOrElse(Subset.beHiking, 0) / 1000),
            number(count.counts.getOrElse(Subset.beBicycle, 0) / 1000),
            number(count.counts.getOrElse(Subset.beHorseRiding, 0) / 1000),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            number(count.counts.getOrElse(Subset.deHiking, 0) / 1000),
            number(count.counts.getOrElse(Subset.deBicycle, 0) / 1000),
            number(count.counts.getOrElse(Subset.deHorseRiding, 0) / 1000),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            number(count.counts.getOrElse(Subset.frHiking, 0) / 1000),
            number(count.counts.getOrElse(Subset.frBicycle, 0) / 1000),
            number(count.counts.getOrElse(Subset.frHorseRiding, 0) / 1000),
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
            number(count.counts.getOrElse(Subset.nlHiking, 0) / 1000),
            number(count.counts.getOrElse(Subset.nlBicycle, 0) / 1000),
            number(count.counts.getOrElse(Subset.nlHorseRiding, 0) / 1000),
            number(count.counts.getOrElse(Subset.nlMotorboat, 0) / 1000),
            number(count.counts.getOrElse(Subset.nlCanoe, 0) / 1000),
            number(count.counts.getOrElse(Subset.nlInlineSkates, 0) / 1000)
          ),
          CountryStatistic(
            number(count.counts.getOrElse(Subset.beHiking, 0) / 1000),
            number(count.counts.getOrElse(Subset.beBicycle, 0) / 1000),
            number(count.counts.getOrElse(Subset.beHorseRiding, 0) / 1000),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            number(count.counts.getOrElse(Subset.deHiking, 0) / 1000),
            number(count.counts.getOrElse(Subset.deBicycle, 0) / 1000),
            number(count.counts.getOrElse(Subset.deHorseRiding, 0) / 1000),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            number(count.counts.getOrElse(Subset.frHiking, 0) / 1000),
            number(count.counts.getOrElse(Subset.frBicycle, 0) / 1000),
            number(count.counts.getOrElse(Subset.frHorseRiding, 0) / 1000),
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
            percentage(count.counts.getOrElse(Subset.nlHiking, 0) - failed.counts.getOrElse(Subset.nlHiking, 0), count.counts.getOrElse(Subset.nlHiking, 0)),
            percentage(count.counts.getOrElse(Subset.nlBicycle, 0) - failed.counts.getOrElse(Subset.nlBicycle, 0), count.counts.getOrElse(Subset.nlBicycle, 0)),
            percentage(count.counts.getOrElse(Subset.nlHorseRiding, 0) - failed.counts.getOrElse(Subset.nlHorseRiding, 0), count.counts.getOrElse(Subset.nlHorseRiding, 0)),
            percentage(count.counts.getOrElse(Subset.nlMotorboat, 0) - failed.counts.getOrElse(Subset.nlMotorboat, 0), count.counts.getOrElse(Subset.nlMotorboat, 0)),
            percentage(count.counts.getOrElse(Subset.nlCanoe, 0) - failed.counts.getOrElse(Subset.nlCanoe, 0), count.counts.getOrElse(Subset.nlCanoe, 0)),
            percentage(count.counts.getOrElse(Subset.nlInlineSkates, 0) - failed.counts.getOrElse(Subset.nlInlineSkates, 0), count.counts.getOrElse(Subset.nlInlineSkates, 0))
          ),
          CountryStatistic(
            percentage(count.counts.getOrElse(Subset.beHiking, 0) - failed.counts.getOrElse(Subset.beHiking, 0), count.counts.getOrElse(Subset.beHiking, 0)),
            percentage(count.counts.getOrElse(Subset.beBicycle, 0) - failed.counts.getOrElse(Subset.beBicycle, 0), count.counts.getOrElse(Subset.beBicycle, 0)),
            percentage(count.counts.getOrElse(Subset.beHorseRiding, 0) - failed.counts.getOrElse(Subset.beHorseRiding, 0), count.counts.getOrElse(Subset.beHorseRiding, 0)),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            percentage(count.counts.getOrElse(Subset.deHiking, 0) - failed.counts.getOrElse(Subset.deHiking, 0), count.counts.getOrElse(Subset.deHiking, 0)),
            percentage(count.counts.getOrElse(Subset.deBicycle, 0) - failed.counts.getOrElse(Subset.deBicycle, 0), count.counts.getOrElse(Subset.deBicycle, 0)),
            percentage(count.counts.getOrElse(Subset.deHorseRiding, 0) - failed.counts.getOrElse(Subset.deHorseRiding, 0), count.counts.getOrElse(Subset.deHorseRiding, 0)),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            percentage(count.counts.getOrElse(Subset.frHiking, 0) - failed.counts.getOrElse(Subset.frHiking, 0), count.counts.getOrElse(Subset.frHiking, 0)),
            percentage(count.counts.getOrElse(Subset.frBicycle, 0) - failed.counts.getOrElse(Subset.frBicycle, 0), count.counts.getOrElse(Subset.frBicycle, 0)),
            percentage(count.counts.getOrElse(Subset.frHorseRiding, 0) - failed.counts.getOrElse(Subset.frHorseRiding, 0), count.counts.getOrElse(Subset.frHorseRiding, 0)),
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
            percentage(checkCount.counts.getOrElse(Subset.nlHiking, 0), nodeCount.counts.getOrElse(Subset.nlHiking, 0)),
            percentage(checkCount.counts.getOrElse(Subset.nlBicycle, 0), nodeCount.counts.getOrElse(Subset.nlBicycle, 0)),
            percentage(checkCount.counts.getOrElse(Subset.nlHorseRiding, 0), nodeCount.counts.getOrElse(Subset.nlHorseRiding, 0)),
            percentage(checkCount.counts.getOrElse(Subset.nlMotorboat, 0), nodeCount.counts.getOrElse(Subset.nlMotorboat, 0)),
            percentage(checkCount.counts.getOrElse(Subset.nlCanoe, 0), nodeCount.counts.getOrElse(Subset.nlCanoe, 0)),
            percentage(checkCount.counts.getOrElse(Subset.nlInlineSkates, 0), nodeCount.counts.getOrElse(Subset.nlInlineSkates, 0))
          ),
          CountryStatistic(
            percentage(checkCount.counts.getOrElse(Subset.beHiking, 0), nodeCount.counts.getOrElse(Subset.beHiking, 0)),
            percentage(checkCount.counts.getOrElse(Subset.beBicycle, 0), nodeCount.counts.getOrElse(Subset.beBicycle, 0)),
            percentage(checkCount.counts.getOrElse(Subset.beHorseRiding, 0), nodeCount.counts.getOrElse(Subset.beHorseRiding, 0)),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            percentage(checkCount.counts.getOrElse(Subset.deHiking, 0), nodeCount.counts.getOrElse(Subset.deHiking, 0)),
            percentage(checkCount.counts.getOrElse(Subset.deBicycle, 0), nodeCount.counts.getOrElse(Subset.deBicycle, 0)),
            percentage(checkCount.counts.getOrElse(Subset.deHorseRiding, 0), nodeCount.counts.getOrElse(Subset.deHorseRiding, 0)),
            "-",
            "-",
            "-"
          ),
          CountryStatistic(
            percentage(checkCount.counts.getOrElse(Subset.frHiking, 0), nodeCount.counts.getOrElse(Subset.frHiking, 0)),
            percentage(checkCount.counts.getOrElse(Subset.frBicycle, 0), nodeCount.counts.getOrElse(Subset.frBicycle, 0)),
            percentage(checkCount.counts.getOrElse(Subset.frHorseRiding, 0), nodeCount.counts.getOrElse(Subset.frHorseRiding, 0)),
            "-",
            "-",
            "-"
          )
        )
      }
    }
  }

}
