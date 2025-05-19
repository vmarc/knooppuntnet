package kpn.database.actions.statistics

import kpn.api.common.Country
import kpn.api.common.Country.de
import kpn.api.common.Country.nl
import kpn.api.common.RouteType
import kpn.api.common.RouteType.cycling
import kpn.api.common.RouteType.hiking
import kpn.core.doc.Label
import kpn.core.test.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater

class StatisticsUpdateSubsetRouteDistanceTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>

      buildRoute(database, 11L, nl, hiking, 1000)
      buildRoute(database, 12L, nl, hiking, 2000)
      buildRoute(database, 13L, nl, cycling, 3000)
      buildRoute(database, 14L, de, hiking, 4000)
      buildRoute(database, 15L, de, hiking, 5000)
      buildRoute(database, 16L, de, cycling, 6000)
      buildRoute(database, 17L, de, cycling, 7000, active = false)

      new StatisticsUpdater(database).execute()
      val counts = new MongoQueryStatistics(database).execute()

      counts should contain(
        StatisticLongValues(
          "Distance",
          Seq(
            StatisticLongValue(de, cycling, 6L),
            StatisticLongValue(de, hiking, 9L),
            StatisticLongValue(nl, cycling, 3L),
            StatisticLongValue(nl, hiking, 3L),
          )
        )
      )
    }
  }

  private def buildRoute(database: Database, routeId: Long, country: Country, routeType: RouteType, meters: Int, active: Boolean = true): Unit = {
    database.routes.save(
      newRouteDoc(
        newRouteSummary(
          routeId,
          Seq(country),
          routeTypes = Seq(routeType),
          meters = meters
        ),
        labels = if (active) Seq(Label.active) else Seq.empty
      )
    )
  }
}
