package kpn.database.actions.statistics

import kpn.api.common.Country
import kpn.api.common.Country.de
import kpn.api.common.Country.nl
import kpn.api.common.RouteType
import kpn.api.common.RouteType.cycling
import kpn.api.common.RouteType.hiking
import kpn.core.test.MongoTest
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater

class StatisticsUpdateSubsetOrphanNodeCountTest extends MongoTest {

  test("execute") {
    buildOrphanNodeDoc(1L, nl, hiking)
    buildOrphanNodeDoc(2L, nl, hiking)
    buildOrphanNodeDoc(3L, nl, cycling)
    buildOrphanNodeDoc(4L, de, hiking)
    buildOrphanNodeDoc(5L, de, hiking)
    buildOrphanNodeDoc(6L, de, cycling)

    new StatisticsUpdater(database).execute()
    val counts = new MongoQueryStatistics(database).execute()

    counts should contain(
      StatisticLongValues(
        "OrphanNodeCount",
        Seq(
          StatisticLongValue(de, cycling, 1L),
          StatisticLongValue(de, hiking, 2L),
          StatisticLongValue(nl, cycling, 1L),
          StatisticLongValue(nl, hiking, 2L),
        )
      )
    )
  }

  private def buildOrphanNodeDoc(nodeId: Long, country: Country, routeType: RouteType): Unit = {
    database.orphanNodes.save(
      newOrphanNodeDoc(
        country,
        routeType,
        nodeId
      )
    )
  }
}
