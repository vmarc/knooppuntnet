package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetNetwork
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeKey
import kpn.api.custom.ChangeType
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.doc.Label
import kpn.core.test.OverpassData

class NetworkDeleteTest04 extends IntegrationTest {

  test("network changes to route") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .relation(
        1,
        Seq(
          newMember("node", 1001)
        ),
        Tags.from(
          "network:type" -> "node_network",
          "type" -> "network",
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
          newMember("node", 1001)
        ),
        Tags.from(
          "network:type" -> "node_network",
          "type" -> "route",
          "route" -> "hiking",
          "network" -> "rwn",
          "name" -> "01-02",
        ),
        2
      )

    testIntegration(dataBefore, dataAfter) {

      assert(watched.networks.contains(1))
      assert(!watched.routes.contains(1))

      process(ChangeAction.Modify, dataAfter.data.relations(1).raw)

      assert(!watched.networks.contains(1))
      assert(watched.routes.contains(1))

      assertNetwork()
      assertNetworkInfo()
      assertRoute()

      assertNetworkChange()
      assertChangeSetSummary()
      assertRouteChange()
    }
  }

  private def assertNetwork(): Unit = {
    findNetworkById(1) should matchTo(
      newNetwork(
        1L,
        active = false,
        version = 1,
        tags = Tags.from(
          "network:type" -> "node_network",
          "type" -> "network",
          "network" -> "rwn",
          "name" -> "01-02",
        )
      )
    )
  }

  private def assertNetworkInfo(): Unit = {
    findNetworkInfoById(1) should matchTo(
      newNetworkInfoDoc(
        1L,
        active = false,
        country = Some(Country.nl),
        summary = newNetworkSummary(
          name = "01-02",
          changeCount = 1
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
    route.labels should contain(Label.active)
    route.summary.name should equal("01-02")
    route.version should equal(2)
  }

  private def assertChangeSetSummary(): Unit = {
    findChangeSetSummaryById("123:1") should matchTo(
      newChangeSetSummary(
        key = ChangeKey(1, Timestamp(2015, 8, 11, 0, 0, 0), 123, 0),
        subsets = Seq(Subset.nlHiking),
        timestampFrom = Timestamp(2015, 8, 11, 0, 0, 2),
        timestampUntil = Timestamp(2015, 8, 11, 0, 0, 3),
        networkChanges = NetworkChanges(
          deletes = Seq(
            ChangeSetNetwork(
              country = Some(Country.nl),
              networkType = NetworkType.hiking,
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
        happy = true,
        investigate = true
      )
    )
  }

  private def assertNetworkChange(): Unit = {
    val networkChange = findNetworkInfoChangeById("123:1:1")
    networkChange.key.changeSetId should equal(123)
    networkChange.key.elementId should equal(1)
    networkChange.changeType should equal(ChangeType.Delete)
    networkChange.networkType should equal(NetworkType.hiking)
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
