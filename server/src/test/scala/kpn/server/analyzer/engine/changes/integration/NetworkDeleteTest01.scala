package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetNetwork
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.data.raw.RawMember
import kpn.api.custom.ChangeType
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.test.OverpassData

class NetworkDeleteTest01 extends IntegrationTest {

  test("network delete") {

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

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Delete, newRawRelation(1))

      assert(!watched.networks.contains(1))

      assertNetwork()
      assertNetworkInfo()

      // TODO add delete test where before network contains nodes and routes that become orphan because of the delete
      //    orphanRoutes = RefChanges(newRefs = newOrphanRoutes),
      //    ignoredRoutes = RefChanges(newRefs = newIgnoredRoutes),
      //    orphanNodes = RefChanges(newRefs = newOrphanNodes),
      //    ignoredNodes = RefChanges(newRefs = newIgnoredNodes),

      assertNetworkChange()
      assertChangeSetSummary()
    }
  }

  private def assertNetwork(): Unit = {
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

  private def assertNetworkInfo(): Unit = {
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
        nodeChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs(
              Seq(
                newChangeSetElementRef(1001, "01", investigate = true)
              )
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
}
