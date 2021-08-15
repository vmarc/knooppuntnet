package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.common.Ref
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.test.OverpassData

class NetworkDeleteNodeTest03 extends IntegrationTest {

  test("network delete - node still referenced in orphan route does not become orphan") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .route(11, "01-02", Seq(newMember("node", 1001)))
      .networkRelation(1, "network", Seq(newMember("node", 1001)))

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .route(11, "01-02", Seq(newMember("node", 1001)))

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Delete, newRawRelation(1))

      assert(database.orphanNodes.isEmpty) // <--
      assert(database.routeChanges.isEmpty)

      assert(!watched.networks.contains(1))
      assert(watched.routes.contains(11))
      assert(watched.nodes.contains(1001))

      assertNetworkInfo()
      assertNetworkInfoChange()
      assertNodeChange()
      assertChangeSetSummary()
    }
  }

  private def assertNetworkInfo(): Unit = {
    findNetworkInfoById(1) should matchTo(
      newNetworkInfoDoc(
        1,
        active = false, // <--- !!!
        country = Some(Country.nl),
        newNetworkSummary(
          name = "network",
          networkType = NetworkType.hiking,
        ),
        newNetworkDetail(
          lastUpdated = defaultTimestamp, // TODO MONGO timestampAfterValue,
          relationLastUpdated = defaultTimestamp, // TODO MONGO timestampAfterValue
          tags = newNetworkTags("network")
        )
      )
    )
  }

  private def assertNetworkInfoChange(): Unit = {
    findNetworkInfoChangeById("123:1:1") should matchTo(
      newNetworkInfoChange(
        newChangeKey(elementId = 1),
        ChangeType.Delete,
        Some(Country.nl),
        NetworkType.hiking,
        1,
        "network",
        networkNodes = RefDiffs(
          removed = Seq(Ref(1001, "01"))
        ),
        investigate = true
      )
    )
  }

  private def assertNodeChange(): Unit = {
    findNodeChangeById("123:1:1001") should matchTo(
      newNodeChange(
        key = newChangeKey(elementId = 1001),
        changeType = ChangeType.Update,
        subsets = Seq(Subset.nlHiking),
        name = "01",
        before = Some(
          newMetaData()
        ),
        after = Some(
          newMetaData()
        ),
        removedFromNetwork = Seq(
          Ref(1, "network")
        ),
        investigate = true,
        impact = true
      )
    )
  }

  private def assertChangeSetSummary(): Unit = {
    findChangeSetSummaryById("123:1") should matchTo(
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
        networkChanges = NetworkChanges(
          deletes = Seq(
            newChangeSetNetwork(
              Some(Country.nl),
              NetworkType.hiking,
              1,
              "network",
              nodeChanges = ChangeSetElementRefs(
                removed = Seq(
                  newChangeSetElementRef(1001, "01", investigate = true)
                )
              ),
              investigate = true
            )
          )
        ),
        nodeChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs(
              updated = Seq(
                newChangeSetElementRef(1001, "01", investigate = true
                )
              )
            )
          )
        ),
        subsetAnalyses = Seq(
          ChangeSetSubsetAnalysis(Subset.nlHiking, investigate = true)
        ),
        investigate = true
      )
    )
  }
}
