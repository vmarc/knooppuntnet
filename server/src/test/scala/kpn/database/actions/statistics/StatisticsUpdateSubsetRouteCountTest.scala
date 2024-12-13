package kpn.database.actions.statistics

import kpn.api.common.Country
import kpn.api.common.NetworkType
import kpn.api.common.NetworkType.cycling
import kpn.api.common.NetworkType.hiking
import kpn.api.common.SharedTestObjects
import Country.de
import Country.nl
import kpn.core.doc.Label
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater

class StatisticsUpdateSubsetRouteCountTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>

      buildRoute(database, 11L, nl, hiking)
      buildRoute(database, 12L, nl, hiking)
      buildRoute(database, 13L, nl, cycling)
      buildRoute(database, 14L, de, hiking)
      buildRoute(database, 15L, de, hiking)
      buildRoute(database, 16L, de, cycling)
      buildRoute(database, 17L, de, cycling, active = false)

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
  }

  private def buildRoute(database: Database, routeId: Long, country: Country, networkType: NetworkType, active: Boolean = true): Unit = {
    database.routes.save(
      newRouteDoc(
        newRouteSummary(
          routeId,
          Seq(country),
          networkTypes = Seq(networkType),
        ),
        labels = if (active) Seq(Label.active) else Seq.empty
      )
    )
  }
}
