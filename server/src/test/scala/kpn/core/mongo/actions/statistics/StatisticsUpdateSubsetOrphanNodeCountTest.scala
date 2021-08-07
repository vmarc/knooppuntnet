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

class StatisticsUpdateSubsetOrphanNodeCountTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>

      buildOrphanNodeDoc(database, 1L, nl, hiking)
      buildOrphanNodeDoc(database, 2L, nl, hiking)
      buildOrphanNodeDoc(database, 3L, nl, cycling)
      buildOrphanNodeDoc(database, 4L, de, hiking)
      buildOrphanNodeDoc(database, 5L, de, hiking)
      buildOrphanNodeDoc(database, 6L, de, cycling)

      new StatisticsUpdateSubsetOrphanNodeCount(database).execute()

      val counts = new MongoQueryStatistics(database).execute()
      counts should equal(
        Seq(
          StatisticValues(
            "OrphanNodeCount",
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

  private def buildOrphanNodeDoc(database: Database, nodeId: Long, country: Country, networkType: NetworkType): Unit = {
    database.orphanNodes.save(
      newOrphanNodeDoc(
        country,
        networkType,
        nodeId
      )
    )
  }
}
