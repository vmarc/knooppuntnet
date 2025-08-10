package kpn.database.actions.statistics

import kpn.api.common.Country
import kpn.api.common.Country.de
import kpn.api.common.Country.nl
import kpn.api.common.RouteType
import kpn.api.common.RouteType.cycling
import kpn.api.common.RouteType.hiking
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newNodeDoc
import kpn.core.test.TestObjects.newNodeName
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater

class StatisticsUpdateSubsetNodeCountTest extends MongoTest {

  test("execute") {

    buildNode(1L, nl, hiking)
    buildNode(2L, nl, hiking)
    buildNode(3L, nl, cycling)
    buildNode(4L, de, hiking)
    buildNode(5L, de, hiking)
    buildNode(6L, de, cycling)

    // non-active networks are not included in the statistics
    buildNode(7L, de, cycling, active = false)

    new StatisticsUpdater(database).execute()
    val counts = new MongoQueryStatistics(database).execute()

    assertEqual(
      counts.find(_._id == "NodeCount").get.values.toSet,
      Set(
        StatisticLongValue(de, cycling, 1L),
        StatisticLongValue(de, hiking, 2L),
        StatisticLongValue(nl, cycling, 1L),
        StatisticLongValue(nl, hiking, 2L),
      )
    )
  }

  private def buildNode(nodeId: Long, country: Country, routeType: RouteType, active: Boolean = true): Unit = {
    database.nodes.save(
      newNodeDoc(
        nodeId,
        active = active,
        country = Some(country),
        names = Seq(
          newNodeName(
            routeType = routeType
          )
        )
      )
    )
  }
}
