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

class StatisticsUpdateSubsetNodeCountTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>

      buildNode(database, 1L, nl, hiking)
      buildNode(database, 2L, nl, hiking)
      buildNode(database, 3L, nl, cycling)
      buildNode(database, 4L, de, hiking)
      buildNode(database, 5L, de, hiking)
      buildNode(database, 6L, de, cycling)

      // non-active networks are not included in the statistics
      buildNode(database, 7L, de, cycling, active = false)

      new StatisticsUpdateSubsetNodeCount(database).execute()
      val counts = new MongoQueryStatistics(database).execute()

      counts should equal(
        Seq(
          StatisticValues(
            "NodeCount",
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

  private def buildNode(database: Database, nodeId: Long, country: Country, networkType: NetworkType, active: Boolean = true): Unit = {
    database.nodes.save(
      newNodeDoc(
        nodeId,
        active = active,
        country = Some(country),
        names = Seq(
          newNodeName(
            networkType = networkType
          )
        )
      )
    )
  }
}