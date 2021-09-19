package kpn.core.mongo.actions.statistics

import kpn.api.common.SharedTestObjects
import kpn.api.common.statistics.StatisticValue
import kpn.api.common.statistics.StatisticValues
import kpn.api.custom.Country
import kpn.api.custom.Country.de
import kpn.api.custom.Country.nl
import kpn.api.custom.NetworkType
import kpn.api.custom.NetworkType.cycling
import kpn.api.custom.NetworkType.hiking
import kpn.core.mongo.Database
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class StatisticsUpdateSubsetRouteCountTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>

      buildRouteInfo(database, 11L, nl, hiking)
      buildRouteInfo(database, 12L, nl, hiking)
      buildRouteInfo(database, 13L, nl, cycling)
      buildRouteInfo(database, 14L, de, hiking)
      buildRouteInfo(database, 15L, de, hiking)
      buildRouteInfo(database, 16L, de, cycling)
      buildRouteInfo(database, 17L, de, cycling, active = false)

      new StatisticsUpdateSubsetRouteCount(database).execute()
      val counts = new MongoQueryStatistics(database).execute()

      counts should equal(
        Seq(
          StatisticValues(
            "RouteCount",
            Seq(
              StatisticValue(de, cycling, 1),
              StatisticValue(de, hiking, 2),
              StatisticValue(nl, cycling, 1),
              StatisticValue(nl, hiking, 2),
            )
          )
        )
      )
    }
  }

  private def buildRouteInfo(database: Database, routeId: Long, country: Country, networkType: NetworkType, active: Boolean = true): Unit = {
    database.routes.save(
      newRouteInfo(
        newRouteSummary(
          routeId,
          Some(country),
          networkType,
        ),
        active = active
      )
    )
  }
}
