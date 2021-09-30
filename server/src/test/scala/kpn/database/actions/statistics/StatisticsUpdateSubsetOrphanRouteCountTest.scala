package kpn.database.actions.statistics

import kpn.api.common.SharedTestObjects
import kpn.api.common.statistics.StatisticValue
import kpn.api.common.statistics.StatisticValues
import kpn.api.custom.Country
import kpn.api.custom.Country.de
import kpn.api.custom.Country.nl
import kpn.api.custom.NetworkType
import kpn.api.custom.NetworkType.cycling
import kpn.api.custom.NetworkType.hiking
import kpn.database.base.Database
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class StatisticsUpdateSubsetOrphanRouteCountTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>

      buildOrphanRouteDoc(database, 11L, nl, hiking)
      buildOrphanRouteDoc(database, 12L, nl, hiking)
      buildOrphanRouteDoc(database, 13L, nl, cycling)
      buildOrphanRouteDoc(database, 14L, de, hiking)
      buildOrphanRouteDoc(database, 15L, de, hiking)
      buildOrphanRouteDoc(database, 16L, de, cycling)

      new StatisticsUpdateSubsetOrphanRouteCount(database).execute()
      val counts = new MongoQueryStatistics(database).execute()

      counts should equal(
        Seq(
          StatisticValues(
            "OrphanRouteCount",
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

  private def buildOrphanRouteDoc(database: Database, routeId: Long, country: Country, networkType: NetworkType): Unit = {
    database.orphanRoutes.save(
      newOrphanRouteDoc(
        routeId,
        country,
        networkType,
      )
    )
  }
}
