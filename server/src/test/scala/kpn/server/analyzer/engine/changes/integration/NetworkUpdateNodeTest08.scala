package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.RefChanges
import kpn.api.common.common.Ref
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class NetworkUpdateNodeTest08 extends AbstractIntegrationTest {

  test("network update - an orphan node that is added to the network is no longer orphan") {

    pending

    withDatabase { database =>

      val dataBefore = OverpassData()
        .networkNode(1001, "01")
        .networkNode(1002, "02") // orphan node, not referenced by the network
        .networkRelation(
          1,
          "name",
          Seq(
            newMember("node", 1001)
            // the network does not reference the orphan node
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
            newMember("node", 1002) // reference to the previous orphan node
          )
        )

      val tc = new IntegrationTestContext(database, dataBefore, dataAfter)
      tc.watchOrphanNode(1002)
      tc.watchNetwork(tc.before, 1)

      // before:
      assert(!tc.analysisContext.data.networks.watched.isReferencingNode(1002))
      assert(tc.analysisContext.data.nodes.watched.contains(1002))

      // act:
      tc.process(ChangeAction.Modify, dataAfter.rawRelationWithId(1))

      // after:
      assert(tc.analysisContext.data.networks.watched.isReferencingNode(1002))
      assert(!tc.analysisContext.data.nodes.watched.contains(1002))

      (tc.networkRepository.oldSaveNetworkInfo _).verify(*).once()

      tc.findChangeSetSummaryById("123:1") should matchTo(
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

      tc.findNetworkChangeById("123:1:1") should matchTo(
        newNetworkChange(
          newChangeKey(elementId = 1),
          ChangeType.Update,
          Some(Country.nl),
          NetworkType.hiking,
          1,
          "name",
          orphanNodes = RefChanges(
            oldRefs = Seq(Ref(1002, "02"))
          ),
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

      tc.findNodeChangeById("123:1:1001") should matchTo(
        newNodeChange(
          key = newChangeKey(elementId = 1002),
          changeType = ChangeType.Update,
          subsets = Seq(Subset.nlHiking),
          name = "02",
          before = Some(
            newRawNodeWithName(1002, "02")
          ),
          after = Some(
            newRawNodeWithName(1002, "02")
          ),
          addedToNetwork = Seq(Ref(1, "name")),
          facts = Seq(Fact.WasOrphan),
          happy = true,
          impact = true
        )
      )
    }
  }
}
