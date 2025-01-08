package kpn.database.actions.statistics

import kpn.api.common.Country
import kpn.api.common.Country.de
import kpn.api.common.Country.nl
import kpn.api.common.RouteType
import kpn.api.common.RouteType.cycling
import kpn.api.common.RouteType.hiking
import kpn.api.common.SharedTestObjects
import kpn.core.doc.Label
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater

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

      new StatisticsUpdater(database).execute()
      val counts = new MongoQueryStatistics(database).execute()

      counts should contain(
        StatisticLongValues(
          "NodeCount",
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

  private def buildNode(database: Database, nodeId: Long, country: Country, routeType: RouteType, active: Boolean = true): Unit = {
    database.nodes.save(
      newNodeDoc(
        nodeId,
        labels = if (active) Seq(Label.active) else Seq.empty,
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
