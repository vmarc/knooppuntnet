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

      counts.size should equal(4)
      counts should contain(StatisticValue(nl, hiking, "OrphanRouteCount", 2))
      counts should contain(StatisticValue(nl, cycling, "OrphanRouteCount", 1))
      counts should contain(StatisticValue(de, hiking, "OrphanRouteCount", 2))
      counts should contain(StatisticValue(de, cycling, "OrphanRouteCount", 1))
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
