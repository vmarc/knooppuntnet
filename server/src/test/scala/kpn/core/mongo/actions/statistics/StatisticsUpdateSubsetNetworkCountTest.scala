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

      new StatisticsUpdateSubsetNetworkCount(database).execute()
      val counts = new MongoQueryStatistics(database).execute()

      counts should equal(
        Seq(
          StatisticValues(
            "NetworkCount",
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

  private def buildNetwork(database: Database, networkId: Long, country: Country, networkType: NetworkType, active: Boolean = true): Unit = {
    database.networkInfos.save(
      newNetworkInfoDoc(
        networkId,
        active,
        Some(country),
        newNetworkSummary(networkType = networkType)
      )
    )
  }
}