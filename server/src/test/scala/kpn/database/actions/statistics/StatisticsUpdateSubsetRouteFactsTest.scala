package kpn.database.actions.statistics

import kpn.api.common.SharedTestObjects
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
        StatisticLongValues(
          "RouteBrokenCount",
          Seq(
            StatisticLongValue(de, cycling, 1L),
            StatisticLongValue(de, hiking, 2L),
            StatisticLongValue(nl, cycling, 1L),
            StatisticLongValue(nl, hiking, 2L)
          )
        )
      )
      counts should contain(
        StatisticLongValues(
          "RouteFixmetodoCount",
          Seq(
            StatisticLongValue(nl, hiking, 1L)
          )
        )
      )
      counts should contain(
        StatisticLongValues(
          "RouteInaccessibleCount",
          Seq(
            StatisticLongValue(nl, hiking, 1L)
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
        StatisticLongValues(
          "RouteBrokenCount",
          Seq(
            StatisticLongValue(nl, hiking, 1L)
          )
        )
      )

      buildRoute(database, 11L, nl, hiking, Seq(RouteWithoutWays))

      new StatisticsUpdater(database).execute()
      val counts2 = new MongoQueryStatistics(database).execute()

      counts2 should contain(
        StatisticLongValues(
          "RouteWithoutWaysCount",
          Seq(
            StatisticLongValue(nl, hiking, 1L)
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
