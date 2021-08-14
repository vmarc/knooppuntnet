package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.common.Ref
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.test.OverpassData

class NetworkUpdateNodeTest09 extends IntegrationTest {

  test("network update - an ignored node that is added to the network is no longer ignored") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02") // ignored node, not referenced by the network
      .networkRelation(
        1,
        "name",
        Seq(
          newMember("node", 1001)
          // the network does not reference the ignored node
        )
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .networkRelation(
        1,
        "name",
        Seq(
          newMember("node", 1001),
          newMember("node", 1002) // reference to the previous ignored node
        )
      )

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Modify, dataAfter.rawRelationWithId(1))

      assert(!watched.nodes.contains(1002))

      val networkDoc = findNetworkById(1)
      networkDoc._id should equal(1)

      val networkInfoDoc = findNetworkInfoById(1)
      networkInfoDoc._id should equal(1)

      findChangeSetSummaryById("123:1") should matchTo(
        newChangeSetSummary(
          subsets = Seq(Subset.nlHiking),
          networkChanges = NetworkChanges(
            updates = Seq(
              newChangeSetNetwork(
                Some(Country.nl),
                NetworkType.hiking,
                1,
                "name",
                nodeChanges = ChangeSetElementRefs(
                  added = Seq(newChangeSetElementRef(1002, "02", happy = true))
                ),
                happy = true
              )
            )
          ),
          subsetAnalyses = Seq(
            ChangeSetSubsetAnalysis(Subset.nlHiking, happy = true)
          ),
          happy = true
        )
      )

      findNetworkInfoChangeById("123:1:1") should matchTo(
        newNetworkInfoChange(
          newChangeKey(elementId = 1),
          ChangeType.Update,
          Some(Country.nl),
          NetworkType.hiking,
          1,
          "name",
          networkDataUpdate = Some(
            NetworkDataUpdate(
              newNetworkData(name = "name"),
              newNetworkData(name = "name")
            )
          ),
          networkNodes = RefDiffs(
            added = Seq(Ref(1002, "02"))
          ),
          happy = true
        )
      )

      findNodeChangeById("123:1:1001") should matchTo(
        newNodeChange(
          key = newChangeKey(elementId = 1002),
          changeType = ChangeType.Update,
          subsets = Seq(Subset.nlHiking),
          name = "02",
          before = Some(
            newMetaData()
          ),
          after = Some(
            newMetaData()
          ),
          addedToNetwork = Seq(Ref(1, "name")),
          facts = Seq.empty,
          happy = true,
          impact = true
        )
      )
    }
  }
}
