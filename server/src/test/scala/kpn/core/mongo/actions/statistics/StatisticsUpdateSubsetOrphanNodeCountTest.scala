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

      counts.size should equal(4)
      counts should contain(StatisticValue(nl, hiking, "OrphanNodeCount", 2))
      counts should contain(StatisticValue(nl, cycling, "OrphanNodeCount", 1))
      counts should contain(StatisticValue(de, hiking, "OrphanNodeCount", 2))
      counts should contain(StatisticValue(de, cycling, "OrphanNodeCount", 1))
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
