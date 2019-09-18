package kpn.core.app.stats

import kpn.core.util.Formatter.number
import kpn.core.util.Formatter.percentage
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
            percentage(numerator.nlRwn, denominator.nlRwn),
            percentage(numerator.nlRcn, denominator.nlRcn),
            percentage(numerator.nlRhn, denominator.nlRhn),
            percentage(numerator.nlRmn, denominator.nlRmn),
            percentage(numerator.nlRpn, denominator.nlRpn),
            percentage(numerator.nlRin, denominator.nlRin)
          ),
          CountryStatistic(
            percentage(numerator.beRwn, denominator.beRwn),
            percentage(numerator.beRcn, denominator.beRcn),
            percentage(numerator.beRhn, denominator.beRhn),
            percentage(numerator.beRmn, denominator.beRmn),
            percentage(numerator.beRpn, denominator.beRpn),
            percentage(numerator.beRin, denominator.beRin)
          ),
          CountryStatistic(
            percentage(numerator.deRwn, denominator.deRwn),
            percentage(numerator.deRcn, denominator.deRcn),
            percentage(numerator.deRhn, denominator.deRhn),
            percentage(numerator.deRmn, denominator.deRmn),
            percentage(numerator.deRpn, denominator.deRpn),
            percentage(numerator.deRin, denominator.deRin)
          ),
          CountryStatistic(
            percentage(numerator.frRwn, denominator.frRwn),
            percentage(numerator.frRcn, denominator.frRcn),
            percentage(numerator.frRhn, denominator.frRhn),
            percentage(numerator.frRmn, denominator.frRmn),
            percentage(numerator.frRpn, denominator.frRpn),
            percentage(numerator.frRin, denominator.frRin)
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
            percentage(numerator.nlRwn, denominator.nlRwn),
            percentage(numerator.nlRcn, denominator.nlRcn),
            percentage(numerator.nlRhn, denominator.nlRhn),
            percentage(numerator.nlRmn, denominator.nlRmn),
            percentage(numerator.nlRpn, denominator.nlRpn),
            percentage(numerator.nlRin, denominator.nlRin)
          ),
          CountryStatistic(
            percentage(numerator.beRwn, denominator.beRwn),
            percentage(numerator.beRcn, denominator.beRcn),
            percentage(numerator.beRhn, denominator.beRhn),
            percentage(numerator.beRmn, denominator.beRmn),
            percentage(numerator.beRpn, denominator.beRpn),
            percentage(numerator.beRin, denominator.beRin)
          ),
          CountryStatistic(
            percentage(numerator.deRwn, denominator.deRwn),
            percentage(numerator.deRcn, denominator.deRcn),
            percentage(numerator.deRhn, denominator.deRhn),
            percentage(numerator.deRmn, denominator.deRmn),
            percentage(numerator.deRpn, denominator.deRpn),
            percentage(numerator.deRin, denominator.deRin)
          ),
          CountryStatistic(
            percentage(numerator.frRwn, denominator.frRwn),
            percentage(numerator.frRcn, denominator.frRcn),
            percentage(numerator.frRhn, denominator.frRhn),
            percentage(numerator.frRmn, denominator.frRmn),
            percentage(numerator.frRpn, denominator.frRpn),
            percentage(numerator.frRin, denominator.frRin)
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
            number(count.nlRwn / 1000),
            number(count.nlRcn / 1000),
            number(count.nlRhn / 1000),
            number(count.nlRmn / 1000),
            number(count.nlRpn / 1000),
            number(count.nlRin / 1000)
          ),
          CountryStatistic(
            number(count.beRwn / 1000),
            number(count.beRcn / 1000),
            number(count.beRhn / 1000),
            number(count.beRmn / 1000),
            number(count.beRpn / 1000),
            number(count.beRin / 1000)
          ),
          CountryStatistic(
            number(count.deRwn / 1000),
            number(count.deRcn / 1000),
            number(count.deRhn / 1000),
            number(count.deRmn / 1000),
            number(count.deRpn / 1000),
            number(count.deRin / 1000)
          ),
          CountryStatistic(
            number(count.frRwn / 1000),
            number(count.frRcn / 1000),
            number(count.frRhn / 1000),
            number(count.frRmn / 1000),
            number(count.frRpn / 1000),
            number(count.frRin / 1000)
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
            number(count.nlRwn / 1000),
            number(count.nlRcn / 1000),
            number(count.nlRhn / 1000),
            number(count.nlRmn / 1000),
            number(count.nlRpn / 1000),
            number(count.nlRin / 1000)
          ),
          CountryStatistic(
            number(count.beRwn / 1000),
            number(count.beRcn / 1000),
            number(count.beRhn / 1000),
            number(count.beRmn / 1000),
            number(count.beRpn / 1000),
            number(count.beRin / 1000)
          ),
          CountryStatistic(
            number(count.deRwn / 1000),
            number(count.deRcn / 1000),
            number(count.deRhn / 1000),
            number(count.deRmn / 1000),
            number(count.deRpn / 1000),
            number(count.deRin / 1000)
          ),
          CountryStatistic(
            number(count.frRwn / 1000),
            number(count.frRcn / 1000),
            number(count.frRhn / 1000),
            number(count.frRmn / 1000),
            number(count.frRpn / 1000),
            number(count.frRin / 1000)
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
            percentage(count.nlRwn - failed.nlRwn, count.nlRwn),
            percentage(count.nlRcn - failed.nlRcn, count.nlRcn),
            percentage(count.nlRhn - failed.nlRhn, count.nlRhn),
            percentage(count.nlRmn - failed.nlRmn, count.nlRmn),
            percentage(count.nlRpn - failed.nlRpn, count.nlRpn),
            percentage(count.nlRin - failed.nlRin, count.nlRin)
          ),
          CountryStatistic(
            percentage(count.beRwn - failed.beRwn, count.beRwn),
            percentage(count.beRcn - failed.beRcn, count.beRcn),
            percentage(count.beRhn - failed.beRhn, count.beRhn),
            percentage(count.beRmn - failed.beRmn, count.beRmn),
            percentage(count.beRpn - failed.beRpn, count.beRpn),
            percentage(count.beRin - failed.beRin, count.beRin)
          ),
          CountryStatistic(
            percentage(count.deRwn - failed.deRwn, count.deRwn),
            percentage(count.deRcn - failed.deRcn, count.deRcn),
            percentage(count.deRhn - failed.deRhn, count.deRhn),
            percentage(count.deRmn - failed.deRmn, count.deRmn),
            percentage(count.deRpn - failed.deRpn, count.deRpn),
            percentage(count.deRin - failed.deRin, count.deRin)
          ),
          CountryStatistic(
            percentage(count.frRwn - failed.frRwn, count.frRwn),
            percentage(count.frRcn - failed.frRcn, count.frRcn),
            percentage(count.frRhn - failed.frRhn, count.frRhn),
            percentage(count.frRmn - failed.frRmn, count.frRmn),
            percentage(count.frRpn - failed.frRpn, count.frRpn),
            percentage(count.frRin - failed.frRin, count.frRin)
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
            percentage(checkCount.nlRwn, nodeCount.nlRwn),
            percentage(checkCount.nlRcn, nodeCount.nlRcn),
            percentage(checkCount.nlRhn, nodeCount.nlRhn),
            percentage(checkCount.nlRmn, nodeCount.nlRmn),
            percentage(checkCount.nlRpn, nodeCount.nlRpn),
            percentage(checkCount.nlRin, nodeCount.nlRin)
          ),
          CountryStatistic(
            percentage(checkCount.beRwn, nodeCount.beRwn),
            percentage(checkCount.beRcn, nodeCount.beRcn),
            percentage(checkCount.beRhn, nodeCount.beRhn),
            percentage(checkCount.beRmn, nodeCount.beRmn),
            percentage(checkCount.beRpn, nodeCount.beRpn),
            percentage(checkCount.beRin, nodeCount.beRin)
          ),
          CountryStatistic(
            percentage(checkCount.deRwn, nodeCount.deRwn),
            percentage(checkCount.deRcn, nodeCount.deRcn),
            percentage(checkCount.deRhn, nodeCount.deRhn),
            percentage(checkCount.deRmn, nodeCount.deRmn),
            percentage(checkCount.deRpn, nodeCount.deRpn),
            percentage(checkCount.deRin, nodeCount.deRin)
          ),
          CountryStatistic(
            percentage(checkCount.frRwn, nodeCount.frRwn),
            percentage(checkCount.frRcn, nodeCount.frRcn),
            percentage(checkCount.frRhn, nodeCount.frRhn),
            percentage(checkCount.frRmn, nodeCount.frRmn),
            percentage(checkCount.frRpn, nodeCount.frRpn),
            percentage(checkCount.frRin, nodeCount.frRin)
          )
        )
      }
    }
  }

}
