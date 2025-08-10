package kpn.database.actions.statistics

import kpn.api.common.Country
import kpn.api.common.Country.de
import kpn.api.common.Country.nl
import kpn.api.common.RouteType
import kpn.api.common.RouteType.cycling
import kpn.api.common.RouteType.hiking
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newNetworkDoc
import kpn.core.test.TestObjects.newNetworkSummary
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater

class StatisticsUpdateSubsetNetworkCountTest extends MongoTest {

  test("execute") {

    buildNetwork(1L, nl, hiking)
    buildNetwork(2L, nl, hiking)
    buildNetwork(3L, nl, cycling)
    buildNetwork(4L, de, hiking)
    buildNetwork(5L, de, hiking)
    buildNetwork(6L, de, cycling)

    // non-active networks are not included in the statistics
    buildNetwork(7L, de, cycling, active = false)

    new StatisticsUpdater(database).execute()
    val counts = new MongoQueryStatistics(database).execute()

    assertEqual(
      counts.find(_._id == "NetworkCount").get.values.toSet,
      Set(
        StatisticLongValue(de, cycling, 1L),
        StatisticLongValue(de, hiking, 2L),
        StatisticLongValue(nl, cycling, 1L),
        StatisticLongValue(nl, hiking, 2L),
      )
    )
  }

  private def buildNetwork(networkId: Long, country: Country, routeType: RouteType, active: Boolean = true): Unit = {
    database.networks.save(
      newNetworkDoc(
        networkId,
        active,
        Some(country),
        newNetworkSummary(routeType = routeType)
      )
    )
  }
}
