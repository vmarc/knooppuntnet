package kpn.database.actions.statistics

import kpn.api.common.Country
import kpn.api.common.Country.de
import kpn.api.common.Country.nl
import kpn.api.common.RouteType
import kpn.api.common.RouteType.cycling
import kpn.api.common.RouteType.hiking
import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater

class StatisticsUpdateSubsetNetworkCountTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>

      buildNetwork(database, 1L, nl, hiking)
      buildNetwork(database, 2L, nl, hiking)
      buildNetwork(database, 3L, nl, cycling)
      buildNetwork(database, 4L, de, hiking)
      buildNetwork(database, 5L, de, hiking)
      buildNetwork(database, 6L, de, cycling)

      // non-active networks are not included in the statistics
      buildNetwork(database, 7L, de, cycling, active = false)

      new StatisticsUpdater(database).execute()
      val counts = new MongoQueryStatistics(database).execute()

      counts should contain(
        StatisticLongValues(
          "NetworkCount",
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

  private def buildNetwork(database: Database, networkId: Long, country: Country, routeType: RouteType, active: Boolean = true): Unit = {
    database.networkInfos.save(
      newNetworkInfoDoc(
        networkId,
        active,
        Some(country),
        newNetworkSummary(routeType = routeType)
      )
    )
  }
}
