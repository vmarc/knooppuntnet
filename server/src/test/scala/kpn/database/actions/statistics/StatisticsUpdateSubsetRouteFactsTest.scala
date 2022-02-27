package kpn.database.actions.statistics

import kpn.api.common.SharedTestObjects
import kpn.api.common.statistics.StatisticValue
import kpn.api.common.statistics.StatisticValues
import kpn.api.custom.Country
import kpn.api.custom.Country.de
import kpn.api.custom.Country.nl
import kpn.api.custom.Fact
import kpn.api.custom.Fact.RouteBroken
import kpn.api.custom.Fact.RouteFixmetodo
import kpn.api.custom.Fact.RouteInaccessible
import kpn.api.custom.Fact.RouteWithoutWays
import kpn.api.custom.NetworkType
import kpn.api.custom.NetworkType.cycling
import kpn.api.custom.NetworkType.hiking
import kpn.core.doc.Label
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater

class StatisticsUpdateSubsetRouteFactsTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>

      buildRoute(database, 11L, nl, hiking, Seq(RouteBroken, RouteInaccessible))
      buildRoute(database, 12L, nl, hiking, Seq(RouteBroken, RouteFixmetodo))
      buildRoute(database, 13L, nl, cycling, Seq(RouteBroken))
      buildRoute(database, 14L, de, hiking, Seq(RouteBroken))
      buildRoute(database, 15L, de, hiking, Seq(RouteBroken))
      buildRoute(database, 16L, de, cycling, Seq(RouteBroken))
      buildRoute(database, 17L, de, cycling, Seq(RouteBroken), active = false)

      new StatisticsUpdater(database).execute()
      val counts = new MongoQueryStatistics(database).execute()

      counts should contain(
        StatisticValues(
          "RouteBrokenCount",
          Seq(
            StatisticValue(de, cycling, 1),
            StatisticValue(de, hiking, 2),
            StatisticValue(nl, cycling, 1),
            StatisticValue(nl, hiking, 2)
          )
        )
      )
      counts should contain(
        StatisticValues(
          "RouteFixmetodoCount",
          Seq(
            StatisticValue(nl, hiking, 1)
          )
        )
      )
      counts should contain(
        StatisticValues(
          "RouteInaccessibleCount",
          Seq(
            StatisticValue(nl, hiking, 1)
          )
        )
      )
    }
  }

  test("multiple updates, only last situation reflected in the statistics") {
    withDatabase { database =>

      buildRoute(database, 11L, nl, hiking, Seq(RouteBroken))

      new StatisticsUpdater(database).execute()
      val counts1 = new MongoQueryStatistics(database).execute()

      counts1 should contain(
        StatisticValues(
          "RouteBrokenCount",
          Seq(
            StatisticValue(nl, hiking, 1)
          )
        )
      )

      buildRoute(database, 11L, nl, hiking, Seq(RouteWithoutWays))

      new StatisticsUpdater(database).execute()
      val counts2 = new MongoQueryStatistics(database).execute()

      counts2 should contain(
        StatisticValues(
          "RouteWithoutWaysCount",
          Seq(
            StatisticValue(nl, hiking, 1)
          )
        )
      )

      counts2.filter(_._id == "RouteBrokenCount") should equal(Seq.empty)
    }
  }


  private def buildRoute(database: Database, routeId: Long, country: Country, networkType: NetworkType, facts: Seq[Fact], active: Boolean = true): Unit = {
    database.routes.save(
      newRouteDoc(
        newRouteSummary(
          routeId,
          Some(country),
          networkType,
        ),
        labels = if (active) Seq(Label.active) else Seq.empty,
        facts = facts
      )
    )
  }
}
