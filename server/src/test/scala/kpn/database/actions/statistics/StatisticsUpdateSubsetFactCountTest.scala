package kpn.database.actions.statistics

import kpn.api.common.Country
import kpn.api.common.Country.de
import kpn.api.common.Country.nl
import kpn.api.common.Fact
import kpn.api.common.NetworkFact
import kpn.api.common.RouteType
import kpn.api.common.RouteType.cycling
import kpn.api.common.RouteType.hiking
import kpn.core.test.MongoTest
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater

class StatisticsUpdateSubsetFactCountTest extends MongoTest {

  test("network fact counts") {

    buildNetworks()

    new StatisticsUpdater(database).execute()
    val counts = new MongoQueryStatistics(database).execute()

    counts should contain(
      StatisticLongValues(
        "FactCount",
        Seq(
          StatisticLongValue(de, cycling, 1L),
          StatisticLongValue(de, hiking, 2L),
          StatisticLongValue(nl, hiking, 4L),
        )
      )
    )
  }

  test("route fact counts") {

    buildRoutes()

    new StatisticsUpdater(database).execute()
    val counts = new MongoQueryStatistics(database).execute()

    counts should contain(
      StatisticLongValues(
        "FactCount",
        Seq(
          StatisticLongValue(nl, hiking, 1L),
        )
      )
    )
  }

  test("node fact counts") {

    buildNodes()

    new StatisticsUpdater(database).execute()
    val counts = new MongoQueryStatistics(database).execute()

    counts should contain(
      StatisticLongValues(
        "FactCount",
        Seq(
          StatisticLongValue(de, cycling, 1L),
          StatisticLongValue(de, hiking, 2L),
          StatisticLongValue(nl, hiking, 4L),
        )
      )
    )
  }

  test("total fact counts") {

    buildNetworks()
    buildRoutes()
    buildNodes()

    new StatisticsUpdater(database).execute()
    val counts = new MongoQueryStatistics(database).execute()

    counts should contain(
      StatisticLongValues(
        "FactCount",
        Seq(
          StatisticLongValue(de, cycling, 2L),
          StatisticLongValue(de, hiking, 4L),
          StatisticLongValue(nl, hiking, 9L),
        )
      )
    )
  }

  private def buildNetworks(): Unit = {
    buildNetwork(1L, nl, hiking, Seq(NetworkFact(Fact.NetworkExtraMemberNode, elementIds = Some(Seq(1001))), NetworkFact(Fact.NetworkExtraMemberWay, elementIds = Some(Seq(1001)))))
    buildNetwork(2L, nl, hiking, Seq(NetworkFact(Fact.NetworkExtraMemberNode, elementIds = Some(Seq(1001))), NetworkFact(Fact.NetworkExtraMemberRelation, elementIds = Some(Seq(1001)))))
    buildNetwork(3L, nl, hiking, Seq.empty)
    buildNetwork(4L, de, hiking, Seq(NetworkFact(Fact.NetworkExtraMemberNode, elementIds = Some(Seq(1001)))))
    buildNetwork(5L, de, hiking, Seq(NetworkFact(Fact.NetworkExtraMemberNode, elementIds = Some(Seq(1001)))))
    buildNetwork(6L, de, cycling, Seq(NetworkFact(Fact.NetworkExtraMemberNode, elementIds = Some(Seq(1001)))))
    buildNetwork(7L, de, cycling, Seq(NetworkFact(Fact.NetworkExtraMemberNode, elementIds = Some(Seq(1001)))), active = false)
  }

  private def buildNetwork(
    networkId: Long,
    country: Country,
    routeType: RouteType,
    facts: Seq[NetworkFact],
    active: Boolean = true
  ): Unit = {
    database.networks.save(
      newNetworkDoc(
        networkId,
        active,
        Some(country),
        newNetworkSummary(routeType = routeType),
        facts = facts
      )
    )
  }

  private def buildRoutes(): Unit = {
    buildRoute(11L, nl, hiking, Seq(Fact.RouteBroken, Fact.RouteNotForward, Fact.RouteInaccessible))
    buildRoute(12L, nl, hiking, Seq(Fact.RouteBroken, Fact.RouteNotForward, Fact.RouteNotBackward))
    buildRoute(13L, nl, hiking, Seq.empty)
    buildRoute(14L, de, hiking, Seq(Fact.RouteBroken, Fact.RouteNotForward))
    buildRoute(15L, de, hiking, Seq(Fact.RouteBroken, Fact.RouteNotForward))
    buildRoute(16L, de, cycling, Seq(Fact.RouteBroken, Fact.RouteNotForward))
    buildRoute(17L, de, cycling, Seq(Fact.RouteBroken, Fact.RouteNotForward), active = false)
  }

  private def buildRoute(
    routeId: Long,
    country: Country,
    routeType: RouteType,
    facts: Seq[Fact],
    active: Boolean = true
  ): Unit = {
    database.routes.save(
      newRouteDoc(
        newRouteSummary(
          routeId,
          Seq(country),
          routeTypes = Seq(routeType),
        ),
        active = active,
        facts = facts
      )
    )
  }

  private def buildNodes(): Unit = {
    buildNode(1001L, nl, hiking, Seq(Fact.Added, Fact.IntegrityCheckFailed))
    buildNode(1002L, nl, hiking, Seq(Fact.Added, Fact.IntegrityCheckFailed))
    buildNode(1003L, nl, hiking, Seq.empty)
    buildNode(1004L, de, hiking, Seq(Fact.Added))
    buildNode(1005L, de, hiking, Seq(Fact.Added))
    buildNode(1006L, de, cycling, Seq(Fact.Added))
    buildNode(1007L, de, cycling, Seq(Fact.Added), active = false)
  }

  private def buildNode(
    nodeId: Long,
    country: Country,
    routeType: RouteType,
    facts: Seq[Fact],
    active: Boolean = true
  ): Unit = {
    database.nodes.save(
      newNodeDoc(
        nodeId,
        active = active,
        country = Some(country),
        names = Seq(
          newNodeName(
            routeType = routeType
          )
        ),
        facts = facts
      )
    )
  }
}
