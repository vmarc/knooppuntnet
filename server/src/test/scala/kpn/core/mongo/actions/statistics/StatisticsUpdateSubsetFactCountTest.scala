package kpn.core.mongo.actions.statistics

import kpn.api.common.NetworkFact
import kpn.api.common.SharedTestObjects
import kpn.api.common.statistics.StatisticValue
import kpn.api.custom.Country
import kpn.api.custom.Country.de
import kpn.api.custom.Country.nl
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.NetworkType.cycling
import kpn.api.custom.NetworkType.hiking
import kpn.core.mongo.Database
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class StatisticsUpdateSubsetFactCountTest extends UnitTest with SharedTestObjects {

  test("network fact counts") {
    withDatabase { database =>

      buildNetworks(database)

      new StatisticsUpdateSubsetFactCount(database).execute()
      val counts = new MongoQueryStatistics(database).execute()

      counts.size should equal(3)
      counts should contain(kpn.api.common.statistics.StatisticValue(nl, hiking, "FactCount", 4))
      counts should contain(kpn.api.common.statistics.StatisticValue(de, hiking, "FactCount", 2))
      counts should contain(kpn.api.common.statistics.StatisticValue(de, cycling, "FactCount", 1))
    }
  }

  test("route fact counts") {
    withDatabase { database =>

      buildRoutes(database)

      new StatisticsUpdateSubsetFactCount(database).execute()
      val counts = new MongoQueryStatistics(database).execute()

      counts.size should equal(3)
      counts should contain(kpn.api.common.statistics.StatisticValue(nl, hiking, "FactCount", 4))
      counts should contain(kpn.api.common.statistics.StatisticValue(de, hiking, "FactCount", 2))
      counts should contain(kpn.api.common.statistics.StatisticValue(de, cycling, "FactCount", 1))
    }
  }

  test("node fact counts") {
    withDatabase { database =>

      buildNodes(database)

      new StatisticsUpdateSubsetFactCount(database).execute()
      val counts = new MongoQueryStatistics(database).execute()

      counts.size should equal(3)
      counts should contain(kpn.api.common.statistics.StatisticValue(nl, hiking, "FactCount", 4))
      counts should contain(StatisticValue(de, hiking, "FactCount", 2))
      counts should contain(kpn.api.common.statistics.StatisticValue(de, cycling, "FactCount", 1))
    }
  }

  test("total fact counts") {
    withDatabase { database =>

      buildNetworks(database)
      buildRoutes(database)
      buildNodes(database)

      new StatisticsUpdateSubsetFactCount(database).execute()
      val counts = new MongoQueryStatistics(database).execute()

      counts.size should equal(3)
      counts should contain(kpn.api.common.statistics.StatisticValue(nl, hiking, "FactCount", 12))
      counts should contain(kpn.api.common.statistics.StatisticValue(de, hiking, "FactCount", 6))
      counts should contain(kpn.api.common.statistics.StatisticValue(de, cycling, "FactCount", 3))
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
    buildRoute(database, 11L, nl, hiking, Seq(Fact.RouteNotForward, Fact.RouteUnaccessible))
    buildRoute(database, 12L, nl, hiking, Seq(Fact.RouteNotForward, Fact.RouteNotBackward))
    buildRoute(database, 13L, nl, hiking, Seq.empty)
    buildRoute(database, 14L, de, hiking, Seq(Fact.RouteNotForward))
    buildRoute(database, 15L, de, hiking, Seq(Fact.RouteNotForward))
    buildRoute(database, 16L, de, cycling, Seq(Fact.RouteNotForward))
    buildRoute(database, 17L, de, cycling, Seq(Fact.RouteNotForward), active = false)
  }

  private def buildRoute(
    database: Database,
    routeId: Long,
    country: Country,
    networkType: NetworkType,
    facts: Seq[Fact],
    active: Boolean = true
  ): Unit =
 {
    database.routes.save(
      newRouteInfo(
        newRouteSummary(
          routeId,
          Some(country),
          networkType,
        ),
        labels = if (active) {
          Seq("active")
        }
        else {
          Seq.empty
        },
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
      newNodeInfo(
        nodeId,
        active = active,
        country = Some(country),
        names = Seq(
          newNodeName(
            networkType = networkType
          )
        ),
        labels = if (active) {
          Seq("active")
        }
        else {
          Seq.empty
        },
        facts = facts
      )
    )
  }
}
