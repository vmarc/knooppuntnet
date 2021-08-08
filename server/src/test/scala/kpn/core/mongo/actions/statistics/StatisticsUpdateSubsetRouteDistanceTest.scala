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

class StatisticsUpdateSubsetRouteDistanceTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>

      buildRouteInfo(database, 11L, nl, hiking, 1000)
      buildRouteInfo(database, 12L, nl, hiking, 2000)
      buildRouteInfo(database, 13L, nl, cycling, 3000)
      buildRouteInfo(database, 14L, de, hiking, 4000)
      buildRouteInfo(database, 15L, de, hiking, 5000)
      buildRouteInfo(database, 16L, de, cycling, 6000)
      buildRouteInfo(database, 17L, de, cycling, 7000, active = false)

      new StatisticsUpdateSubsetRouteDistance(database).execute()
      val counts = new MongoQueryStatistics(database).execute()

      counts should equal(
        Seq(
          StatisticValues(
            "Distance",
            Seq(
              StatisticValue(de, cycling, 6),
              StatisticValue(de, hiking, 9),
              StatisticValue(nl, cycling, 3),
              StatisticValue(nl, hiking, 3),
            )
          )
        )
      )
    }
  }

  private def buildRouteInfo(database: Database, routeId: Long, country: Country, networkType: NetworkType, meters: Int, active: Boolean = true): Unit = {
    database.routes.save(
      newRouteInfo(
        newRouteSummary(
          routeId,
          Some(country),
          networkType,
          meters = meters
        ),
        labels = if (active) {
          Seq("active")
        }
        else {
          Seq.empty
        },
        active
      )
    )
  }
}