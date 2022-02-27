package kpn.database.actions.statistics

import kpn.api.common.NetworkFact
import kpn.api.common.SharedTestObjects
import kpn.api.common.statistics.StatisticValue
import kpn.api.common.statistics.StatisticValues
import kpn.api.custom.Country
import kpn.api.custom.Country.de
import kpn.api.custom.Country.nl
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.NetworkType.cycling
import kpn.api.custom.NetworkType.hiking
import kpn.core.doc.Label
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater

class StatisticsUpdateSubsetFactCountTest extends UnitTest with SharedTestObjects {

  test("network fact counts") {
    withDatabase { database =>

      buildNetworks(database)

      new StatisticsUpdater(database).execute()
      val counts = new MongoQueryStatistics(database).execute()

      counts should contain(
        StatisticValues(
          "FactCount",
          Seq(
            StatisticValue(de, cycling, 1),
            StatisticValue(de, hiking, 2),
            StatisticValue(nl, hiking, 4),
          )
        )
      )
    }
  }

  test("route fact counts") {
    withDatabase { database =>

      buildRoutes(database)

      new StatisticsUpdater(database).execute()
      val counts = new MongoQueryStatistics(database).execute()

      counts should contain(
        StatisticValues(
          "FactCount",
          Seq(
            StatisticValue(nl, hiking, 1),
          )
        )
      )
    }
  }

  test("node fact counts") {
    withDatabase { database =>

      buildNodes(database)

      new StatisticsUpdater(database).execute()
      val counts = new MongoQueryStatistics(database).execute()

      counts should contain(
        StatisticValues(
          "FactCount",
          Seq(
            StatisticValue(de, cycling, 1),
            StatisticValue(de, hiking, 2),
            StatisticValue(nl, hiking, 4),
          )
        )
      )
    }
  }

  test("total fact counts") {
    withDatabase { database =>

      buildNetworks(database)
      buildRoutes(database)
      buildNodes(database)

      new StatisticsUpdater(database).execute()
      val counts = new MongoQueryStatistics(database).execute()

      counts should contain(
        StatisticValues(
          "FactCount",
          Seq(
            StatisticValue(de, cycling, 2),
            StatisticValue(de, hiking, 4),
            StatisticValue(nl, hiking, 9),
          )
        )
      )
    }
  }

  private def buildNetworks(database: Database): Unit = {
    buildNetwork(database, 1L, nl, hiking, Seq(NetworkFact("fact1"), NetworkFact("fact2")))
    buildNetwork(database, 2L, nl, hiking, Seq(NetworkFact("fact1"), NetworkFact("fact3")))
    buildNetwork(database, 3L, nl, hiking, Seq.empty)
    buildNetwork(database, 4L, de, hiking, Seq(NetworkFact("fact1")))
    buildNetwork(database, 5L, de, hiking, Seq(NetworkFact("fact1")))
    buildNetwork(database, 6L, de, cycling, Seq(NetworkFact("fact1")))
    buildNetwork(database, 7L, de, cycling, Seq(NetworkFact("fact-1")), active = false)
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

  private def buildRoutes(database: Database): Unit = {
    buildRoute(database, 11L, nl, hiking, Seq(Fact.RouteBroken, Fact.RouteNotForward, Fact.RouteInaccessible))
    buildRoute(database, 12L, nl, hiking, Seq(Fact.RouteBroken, Fact.RouteNotForward, Fact.RouteNotBackward))
    buildRoute(database, 13L, nl, hiking, Seq.empty)
    buildRoute(database, 14L, de, hiking, Seq(Fact.RouteBroken, Fact.RouteNotForward))
    buildRoute(database, 15L, de, hiking, Seq(Fact.RouteBroken, Fact.RouteNotForward))
    buildRoute(database, 16L, de, cycling, Seq(Fact.RouteBroken, Fact.RouteNotForward))
    buildRoute(database, 17L, de, cycling, Seq(Fact.RouteBroken, Fact.RouteNotForward), active = false)
  }

  private def buildRoute(
    database: Database,
    routeId: Long,
    country: Country,
    networkType: NetworkType,
    facts: Seq[Fact],
    active: Boolean = true
  ): Unit = {
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

  private def buildNodes(database: Database): Unit = {
    buildNode(database, 1001L, nl, hiking, Seq(Fact.Added, Fact.IntegrityCheckFailed))
    buildNode(database, 1002L, nl, hiking, Seq(Fact.Added, Fact.IntegrityCheckFailed))
    buildNode(database, 1003L, nl, hiking, Seq.empty)
    buildNode(database, 1004L, de, hiking, Seq(Fact.Added))
    buildNode(database, 1005L, de, hiking, Seq(Fact.Added))
    buildNode(database, 1006L, de, cycling, Seq(Fact.Added))
    buildNode(database, 1007L, de, cycling, Seq(Fact.Added), active = false)
  }

  private def buildNode(
    database: Database,
    nodeId: Long,
    country: Country,
    networkType: NetworkType,
    facts: Seq[Fact],
    active: Boolean = true
  ): Unit = {
    database.nodes.save(
      newNodeDoc(
        nodeId,
        labels = if (active) Seq(Label.active) else Seq.empty,
        country = Some(country),
        names = Seq(
          newNodeName(
            networkType = networkType
          )
        ),
        facts = facts
      )
    )
  }
}
