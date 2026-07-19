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
import kpn.api.common.data.MemberType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newBaseNetworkDoc
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newChangeSetElementRef
import kpn.core.test.TestObjects.newChangeSetSummary
import kpn.core.test.TestObjects.newLocationChanges
import kpn.core.test.TestObjects.newMember
import kpn.core.test.TestObjects.newNetworkBaseData
import kpn.core.test.TestObjects.newNetworkDoc
import kpn.core.test.TestObjects.newRaw

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

      processRelation(ChangeAction.Modify, dataAfter.data.relations(1).toRaw)

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
        base = newNetworkBaseData(
          raw = newRaw(
            version = 1,
            tags = Tags.from(
              "network:type" -> "node_network",
              "type" -> "network",
              "network" -> "rwn",
              "name" -> "01-02"
            )
          ),
          name = Some("01-02"),
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
        base = newNetworkBaseData(
          raw = newRaw(
            version = 1,
            tags = Tags.from(
              "network:type" -> "node_network",
              "type" -> "network",
              "network" -> "rwn",
              "name" -> "01-02"
            )
          ),
          name = Some("01-02")
        ),
        country = Some(Country.nl)
      )
    )
  }

  private def assertRoute(): Unit = {
    val route = findRouteById(1)
    route.active should equal(true)
    route.base.name should equal("01-02")
    route.base.raw.version should equal(2)
  }

  private def assertChangeSetSummary(): Unit = {
    assertEqual(
      findChangeSetSummaryById("1:1"),
      newChangeSetSummary(
        key = newChangeKey(),
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
              networkName = Some("01-02"),
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
                newChangeSetElementRef(1, "01-02", happy = true)
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
    val networkChange = findNetworkChangeById("1:1:1")
    networkChange.key.changeSetId should equal(1)
    networkChange.key.elementId should equal(1)
    networkChange.changeType should equal(ChangeType.Delete)
    networkChange.routeType should equal(RouteType.hiking)
    networkChange.networkName should equal(Some("01-02"))
    networkChange.happy should equal(false)
    networkChange.investigate should equal(true)
  }

  private def assertRouteChange(): Unit = {
    val routeChange = findRouteChangeById("1:1:1")
    routeChange.key.changeSetId should equal(1)
    routeChange.key.elementId should equal(1)
    routeChange.changeType should equal(ChangeType.Create)
    routeChange.happy should equal(true)
    routeChange.investigate should equal(false)
  }
}
