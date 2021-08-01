package kpn.core.mongo.actions.statistics

import kpn.api.common.SharedTestObjects
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

      buildRouteInfo(database, 11L, nl, hiking, 100)
      buildRouteInfo(database, 12L, nl, hiking, 200)
      buildRouteInfo(database, 13L, nl, cycling, 300)
      buildRouteInfo(database, 14L, de, hiking, 400)
      buildRouteInfo(database, 15L, de, hiking, 500)
      buildRouteInfo(database, 16L, de, cycling, 600)
      buildRouteInfo(database, 17L, de, cycling, 700, active = false)

      new StatisticsUpdateSubsetRouteDistance(database).execute()

      val counts = new MongoQueryStatistics(database).execute()

      counts.size should equal(4)
      counts should contain(StatisticValue(nl, hiking, "Distance", 300))
      counts should contain(StatisticValue(nl, cycling, "Distance", 300))
      counts should contain(StatisticValue(de, hiking, "Distance", 900))
      counts should contain(StatisticValue(de, cycling, "Distance", 600))
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
