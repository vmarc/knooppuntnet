package kpn.core.mongo.actions.statistics

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.core.mongo.Database
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class StatisticsUpdateSubsetNodeCountTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>

      buildNode(database, 1L, Country.nl, NetworkType.hiking)
      buildNode(database, 2L, Country.nl, NetworkType.hiking)
      buildNode(database, 3L, Country.nl, NetworkType.cycling)
      buildNode(database, 4L, Country.be, NetworkType.hiking)
      buildNode(database, 5L, Country.be, NetworkType.hiking)
      buildNode(database, 6L, Country.be, NetworkType.cycling)

      // non-active networks are not included in the statistics
      buildNode(database, 7L, Country.be, NetworkType.cycling, active = false)

      new StatisticsUpdateSubsetNodeCount(database).execute()

      val counts = new MongoQueryStatistics(database).execute()

      counts.size should equal(4)
      counts should contain(StatisticValue(Country.nl, NetworkType.hiking, "NodeCount", 2))
      counts should contain(StatisticValue(Country.nl, NetworkType.cycling, "NodeCount", 1))
      counts should contain(StatisticValue(Country.be, NetworkType.hiking, "NodeCount", 2))
      counts should contain(StatisticValue(Country.be, NetworkType.cycling, "NodeCount", 1))
    }
  }

  private def buildNode(database: Database, nodeId: Long, country: Country, networkType: NetworkType, active: Boolean = true): Unit = {
    database.nodes.save(
      newNodeInfo(
        nodeId,
        labels = if (active) {
          Seq("active")
        }
        else {
          Seq.empty
        },
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
