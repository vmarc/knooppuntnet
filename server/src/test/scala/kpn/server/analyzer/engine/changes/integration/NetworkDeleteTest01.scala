package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetNetwork
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.common.Ref
import kpn.api.common.data.raw.RawMember
import kpn.api.custom.ChangeType
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.test.OverpassData

class NetworkDeleteTest01 extends IntegrationTest {

  test("network and network node delete") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkRelation(
        1,
        "network1",
        members = Seq(
          RawMember("node", 1001, None)
        )
      )

    val dataAfter = OverpassData.empty

    testIntegration(dataBefore, dataAfter, keepDatabaseAfterTest = true) {

      assert(watched.networks.contains(1))
      assert(watched.nodes.contains(1001))

      process(ChangeAction.Delete, newRawRelation(1))

      assert(!watched.networks.contains(1))
      assert(!watched.nodes.contains(1001))

      assertNetworkNonActive()
      assertNetworkInfoNoneActive()
      assertNodeNonActive()

      assertNetworkChange()
      assertNodeChange()
      assertChangeSetSummary()
    }
  }

  private def assertNetworkNonActive(): Unit = {
    findNetworkById(1) should matchTo(
      newNetwork(
        1L,
        active = false,
        tags = Tags.from(
          "network:type" -> "node_network",
          "type" -> "network",
          "network" -> "rwn",
          "name" -> "network1",
        )
      )
    )
  }

  private def assertNetworkInfoNoneActive(): Unit = {
    findNetworkInfoById(1) should matchTo(
      newNetworkInfoDoc(
        1L,
        active = false,
        country = Some(Country.nl),
        summary = newNetworkSummary(
          name = "network1",
          changeCount = 1
        ),
        detail = newNetworkDetail(
          tags = Tags.from(
            "network:type" -> "node_network",
            "type" -> "network",
            "network" -> "rwn",
            "name" -> "network1",
          )
        )
      )
    )
  }

  private def assertNodeNonActive(): Unit = {
    findNodeById(1001L) should matchTo(
      newNodeDoc(
        1001L,
        labels = Seq("network-type-hiking"), // not active anymore !!!
        active = false,
        country = Some(Country.nl),
        name = "01",
        names = Seq(newNodeName(name = "01")),
        tags = Tags.from(
          "rwn_ref" -> "01",
          "network:type" -> "node_network",
        ),
      )
    )
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
              networkName = "network1",
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
        subsetAnalyses = Seq(
          ChangeSetSubsetAnalysis(
            Subset.nlHiking,
            investigate = true
          )
        ),
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
    networkChange.networkName should equal("network1")
    assert(!networkChange.happy)
    assert(networkChange.investigate)
  }

  private def assertNodeChange(): Unit = {
    val nodeChange = findNodeChangeById("123:1:1001")
    nodeChange.key.changeSetId should equal(123)
    nodeChange.key.elementId should equal(1001)
    nodeChange.changeType should equal(ChangeType.Delete)
    nodeChange.subsets should contain(Subset.nlHiking)
    nodeChange.name should equal("01")
    nodeChange.removedFromNetwork should equal(Seq(Ref(1, "network1")))
    nodeChange.facts should equal(Seq(Fact.Deleted))
    assert(!nodeChange.happy)
    assert(nodeChange.investigate)
  }
}
