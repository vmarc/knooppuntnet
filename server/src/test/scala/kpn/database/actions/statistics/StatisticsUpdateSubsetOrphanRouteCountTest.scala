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

class StatisticsUpdateSubsetOrphanRouteCountTest extends MongoTest {

  test("execute") {

    buildRouteDoc(11L, nl, hiking)
    buildRouteDoc(12L, nl, hiking)
    buildRouteDoc(13L, nl, cycling)
    buildRouteDoc(14L, de, hiking)
    buildRouteDoc(15L, de, hiking)
    buildRouteDoc(16L, de, cycling)

    new StatisticsUpdater(database).execute()
    val counts = new MongoQueryStatistics(database).execute()

    assertEqual(
      counts.find(_._id == "OrphanRouteCount").get.values,
      Seq(
        StatisticLongValue(de, cycling, 1L),
        StatisticLongValue(de, hiking, 2L),
        StatisticLongValue(nl, cycling, 1L),
        StatisticLongValue(nl, hiking, 2L),
      )
    )
  }

  private def buildRouteDoc(routeId: Long, country: Country, routeType: RouteType): Unit = {
    database.routes.save(
      newRouteDoc(
        routeId,
        base = newRouteBaseData(
          countries = Seq(country),
          routeTypes = Seq(routeType)
        ),
      )
    )
  }
}
