package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetNetwork
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.NetworkChanges
import kpn.api.common.RouteType
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.data.MemberType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newBaseNetworkDoc
import kpn.core.test.TestObjects.newChangeSetElementRef
import kpn.core.test.TestObjects.newChangeSetSummary
import kpn.core.test.TestObjects.newLocationChanges
import kpn.core.test.TestObjects.newMember
import kpn.core.test.TestObjects.newNetworkDetail
import kpn.core.test.TestObjects.newNetworkDoc
import kpn.core.test.TestObjects.newNetworkSummary

class NetworkDeleteTest04 extends IntegrationTest {

  test("network changes to route") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .relation(
        1,
        Seq(
          newMember(MemberType.Node, 1001)
        ),
        Tags.from(
          "network:type" -> "node_network",
          "type" -> "network", // <-- this will be changed
          "network" -> "rwn",
          "name" -> "01-02",
        ),
        1
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .relation(
        1,
        Seq(
          newMember(MemberType.Node, 1001)
        ),
        Tags.from(
          "network:type" -> "node_network",
          "type" -> "route", // <-- this is the change
          "route" -> "hiking",
          "network" -> "rwn",
          "name" -> "01-02",
        ),
        2
      )

    testIntegration(dataBefore, dataAfter) {

      assert(watched.networks.contains(1))
      assert(!watched.routes.contains(1))

      process(ChangeAction.Modify, dataAfter.data.relations(1).toRaw)

      assert(!watched.networks.contains(1))
      assert(watched.routes.contains(1))

      assertBaseNetwork()
      assertNetwork()
      assertRoute()

      assertNetworkChange()
      assertChangeSetSummary()
      assertRouteChange()
    }
  }

  private def assertBaseNetwork(): Unit = {
    assertEqual(
      findBaseNetworkById(1),
      newBaseNetworkDoc(
        1L,
        active = false,
        name = Some("01-02"),
        version = 1,
        changeSetId = 1,
        tags = Tags.from(
          "network:type" -> "node_network",
          "type" -> "network",
          "network" -> "rwn",
          "name" -> "01-02",
        ),
        nodeIds = Seq(
          1001
        )
      )
    )
  }

  private def assertNetwork(): Unit = {
    assertEqual(
      findNetworkById(1),
      newNetworkDoc(
        1L,
        active = false,
        country = Some(Country.nl),
        summary = newNetworkSummary(
          name = "01-02",
        ),
        detail = newNetworkDetail(
          version = 1,
          tags = Tags.from(
            "network:type" -> "node_network",
            "type" -> "network",
            "network" -> "rwn",
            "name" -> "01-02",
          )
        )
      )
    )
  }

  private def assertRoute(): Unit = {
    val route = findRouteById(1)
    route.active should equal(true)
    route.summary.name should equal("01-02")
    route.version should equal(2)
  }

  private def assertChangeSetSummary(): Unit = {
    assertEqual(
      findChangeSetSummaryById("123:1"),
      newChangeSetSummary(
        key = ChangeKey(1, Timestamp(2015, 8, 11, 0, 0, 0), 123, 0),
        subsets = Seq(Subset.nlHiking),
        locations = Seq("nl"),
        timestampFrom = Timestamp(2015, 8, 11, 0, 0, 2),
        timestampUntil = Timestamp(2015, 8, 11, 0, 0, 3),
        networkChanges = NetworkChanges(
          deletes = Seq(
            ChangeSetNetwork(
              country = Some(Country.nl),
              routeType = RouteType.hiking,
              networkId = 1,
              networkName = "01-02",
              routeChanges = ChangeSetElementRefs(),
              nodeChanges = ChangeSetElementRefs(
                removed = Seq(
                  newChangeSetElementRef(1001, "01", investigate = true)
                )
              ),
              happy = false,
              investigate = true
            )
          )
        ),
        orphanRouteChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs(
              added = Seq(
                newChangeSetElementRef(1, "01-02", happy = true, investigate = true)
              )
            )
          )
        ),
        subsetAnalyses = Seq(
          ChangeSetSubsetAnalysis(
            Subset.nlHiking,
            happy = true,
            investigate = true
          )
        ),
        locationChanges = Seq(
          newLocationChanges(
            routeType = RouteType.hiking,
            locationNames = Seq("nl"),
            nodeChanges = ChangeSetElementRefs(
              updated = Seq(
                newChangeSetElementRef(1001, "01", happy = true)
              )
            ),
            happy = true
          )
        ),
        happy = true,
        investigate = true
      )
    )
  }

  private def assertNetworkChange(): Unit = {
    val networkChange = findNetworkChangeById("123:1:1")
    networkChange.key.changeSetId should equal(123)
    networkChange.key.elementId should equal(1)
    networkChange.changeType should equal(ChangeType.Delete)
    networkChange.routeType should equal(RouteType.hiking)
    networkChange.networkName should equal("01-02")
    assert(!networkChange.happy)
    assert(networkChange.investigate)
  }

  private def assertRouteChange(): Unit = {
    val routeChange = findRouteChangeById("123:1:1")
    routeChange.key.changeSetId should equal(123)
    routeChange.key.elementId should equal(1)
    routeChange.changeType should equal(ChangeType.Create)
    assert(routeChange.happy)
    assert(routeChange.investigate)
  }
}
