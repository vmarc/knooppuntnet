package kpn.database.actions.statistics

import kpn.api.common.Country
import kpn.api.common.Country.de
import kpn.api.common.Country.nl
import kpn.api.common.RouteType
import kpn.api.common.RouteType.cycling
import kpn.api.common.RouteType.hiking
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newRouteBaseData
import kpn.core.test.TestObjects.newRouteDoc
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater

class StatisticsUpdateSubsetRouteDistanceTest extends MongoTest {

  test("execute") {

    buildRoute(11L, nl, hiking, 1000)
    buildRoute(12L, nl, hiking, 2000)
    buildRoute(13L, nl, cycling, 3000)
    buildRoute(14L, de, hiking, 4000)
    buildRoute(15L, de, hiking, 5000)
    buildRoute(16L, de, cycling, 6000)
    buildRoute(17L, de, cycling, 7000, active = false)

    new StatisticsUpdater(database).execute()
    val counts = new MongoQueryStatistics(database).execute()

    assertEqual(
      counts.find(_._id == "Distance").get.values.toSet,
      Set(
        StatisticLongValue(de, cycling, 6L),
        StatisticLongValue(de, hiking, 9L),
        StatisticLongValue(nl, cycling, 3L),
        StatisticLongValue(nl, hiking, 3L),
      )
    )
  }

  private def buildRoute(routeId: Long, country: Country, routeType: RouteType, meters: Int, active: Boolean = true): Unit = {
    database.routes.save(
      newRouteDoc(
        routeId,
        active = active,
        base = newRouteBaseData(
          countries = Seq(country),
          routeTypes = Seq(routeType),
          meters = meters
        )
      )
    )
  }
}
