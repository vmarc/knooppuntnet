package kpn.core.mongo.actions.statistics

import kpn.api.common.NetworkFact
import kpn.api.common.SharedTestObjects
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.core.mongo.Database
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class StatisticsUpdateSubsetNetworkFactsTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>

      buildNetwork(database, 1L, Country.nl, NetworkType.hiking, Seq(NetworkFact("fact1"), NetworkFact("fact2")))
      buildNetwork(database, 2L, Country.nl, NetworkType.hiking, Seq(NetworkFact("fact1"), NetworkFact("fact3")))
      buildNetwork(database, 3L, Country.nl, NetworkType.hiking, Seq.empty)

      //      buildNetwork(database, 4L, Country.be, NetworkType.hiking, Seq(NetworkFact("fact1")))
      //      buildNetwork(database, 5L, Country.be, NetworkType.hiking, Seq(NetworkFact("fact1")))
      //      buildNetwork(database, 6L, Country.be, NetworkType.cycling, Seq(NetworkFact("fact1")))

      // non-active networks are not included in the statistics
      buildNetwork(database, 7L, Country.be, NetworkType.cycling, Seq(NetworkFact("fact-1")), active = false)

      new StatisticsUpdateSubsetNetworkFacts(database).execute()
      val counts = new MongoQueryStatistics(database).execute()

      counts.size should equal(3)
      counts should contain(StatisticValue(Country.nl, NetworkType.hiking, "fact1", 2))
      counts should contain(StatisticValue(Country.nl, NetworkType.hiking, "fact2", 1))
      counts should contain(StatisticValue(Country.nl, NetworkType.hiking, "fact3", 1))

      //      counts should contain(StatisticValue(Country.nl, NetworkType.cycling, "NetworkCount", 1))
      //      counts should contain(StatisticValue(Country.be, NetworkType.hiking, "NetworkCount", 2))
      //      counts should contain(StatisticValue(Country.be, NetworkType.cycling, "NetworkCount", 1))
    }
  }

  private def buildNetwork(
    database: Database,
    networkId: Long,
    country: Country,
    networkType: NetworkType,
    facts: Seq[NetworkFact],
    active: Boolean = true
  ): Unit = {
    database.networkInfos.save(
      newNetworkInfoDoc(
        networkId,
        active,
        Some(country),
        newNetworkSummary(networkType = networkType),
        facts = facts
      )
    )
  }
}
