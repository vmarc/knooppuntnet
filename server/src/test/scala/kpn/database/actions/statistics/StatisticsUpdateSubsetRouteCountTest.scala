package kpn.database.actions.statistics

import kpn.api.common.Country
import kpn.api.common.Country.de
import kpn.api.common.Country.nl
import kpn.api.common.RouteType
import kpn.api.common.RouteType.cycling
import kpn.api.common.RouteType.hiking
import kpn.core.test.MongoTest
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater

class StatisticsUpdateSubsetRouteCountTest extends MongoTest {

  test("execute") {

    buildRoute(11L, nl, hiking)
    buildRoute(12L, nl, hiking)
    buildRoute(13L, nl, cycling)
    buildRoute(14L, de, hiking)
    buildRoute(15L, de, hiking)
    buildRoute(16L, de, cycling)
    buildRoute(17L, de, cycling, active = false)

    new StatisticsUpdater(database).execute()
    val counts = new MongoQueryStatistics(database).execute()

    counts should contain(
      StatisticLongValues(
        "RouteCount",
        Seq(
          StatisticLongValue(de, cycling, 1L),
          StatisticLongValue(de, hiking, 2L),
          StatisticLongValue(nl, cycling, 1L),
          StatisticLongValue(nl, hiking, 2L),
        )
      )
    )
  }

  private def buildRoute(routeId: Long, country: Country, routeType: RouteType, active: Boolean = true): Unit = {
    database.routes.save(
      newRouteDoc(
        newRouteSummary(
          routeId,
          Seq(country),
          routeTypes = Seq(routeType),
        ),
        active = active
      )
    )
  }
}
