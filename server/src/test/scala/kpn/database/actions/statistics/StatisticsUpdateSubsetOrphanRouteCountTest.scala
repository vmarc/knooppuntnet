package kpn.database.actions.statistics

import kpn.api.common.Country
import kpn.api.common.NetworkType
import kpn.api.common.NetworkType.cycling
import kpn.api.common.NetworkType.hiking
import kpn.api.common.SharedTestObjects
import Country.de
import Country.nl
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater

class StatisticsUpdateSubsetOrphanRouteCountTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>

      buildOrphanRouteDoc(database, 11L, nl, hiking)
      buildOrphanRouteDoc(database, 12L, nl, hiking)
      buildOrphanRouteDoc(database, 13L, nl, cycling)
      buildOrphanRouteDoc(database, 14L, de, hiking)
      buildOrphanRouteDoc(database, 15L, de, hiking)
      buildOrphanRouteDoc(database, 16L, de, cycling)

      new StatisticsUpdater(database).execute()
      val counts = new MongoQueryStatistics(database).execute()

      counts should contain(
        StatisticLongValues(
          "OrphanRouteCount",
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
