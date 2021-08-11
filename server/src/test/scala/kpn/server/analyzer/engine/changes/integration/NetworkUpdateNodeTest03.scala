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
import kpn.core.test.TestSupport.withDatabase

class NetworkUpdateNodeTest03 extends AbstractIntegrationTest {

  test("network update - node that is no longer part of the network after update, does not become orphan node if still referenced in another network") {

    pending

    withDatabase { database =>

      val dataBefore = OverpassData()
        .networkNode(1001, "01")
        .networkNode(1002, "02")
        .networkRelation(
          1,
          "name",
          Seq(
            newMember("node", 1001),
            newMember("node", 1002)
          )
        )
        .networkRelation(
          2,
          "name",
          Seq(
            newMember("node", 1001),
            newMember("node", 1002)
          )
        )

      val dataAfter = OverpassData()
        .networkNode(1001, "01")
        .networkNode(1002, "02")
        .networkRelation(
          1,
          "name",
          Seq(
            newMember("node", 1001)
            // node 02 no longer part of the network
          )
        )
        .networkRelation(
          2,
          "name",
          Seq(
            newMember("node", 1001),
            newMember("node", 1002) // node 02 still referenced in other network
          )
        )

      val tc = new IntegrationTestContext(database, dataBefore, dataAfter)
      tc.watchNetwork(tc.before, 1)
      tc.watchNetwork(tc.before, 2)

      // before:
      assert(tc.analysisContext.data.networks.watched.isReferencingNode(1002))
      assert(!tc.analysisContext.data.nodes.watched.contains(1001))

      // act:
      tc.process(ChangeAction.Modify, dataAfter.rawRelationWithId(1))

      // after:
      assert(tc.analysisContext.data.networks.watched.isReferencingNode(1002))
      assert(!tc.analysisContext.data.nodes.watched.contains(1001))

      (tc.networkRepository.oldSaveNetworkInfo _).verify(*).once()
      (tc.nodeRepository.save _).verify(*).never()

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
                  removed = Seq(
                    newChangeSetElementRef(1002, "02", investigate = true)
                  )
                ),
                investigate = true
              )
            )
          ),
          subsetAnalyses = Seq(
            ChangeSetSubsetAnalysis(Subset.nlHiking, investigate = true)
          ),
          investigate = true
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
          networkDataUpdate = Some(
            NetworkDataUpdate(
              newNetworkData(name = "name"),
              newNetworkData(name = "name")
            )
          ),
          networkNodes = RefDiffs(Seq(Ref(1002, "02")), Seq.empty, Seq.empty),
          investigate = true
        )
      )

      tc.findNodeChangeById("123:1:1001") should matchTo(
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
          removedFromNetwork = Seq(Ref(1, "name")),
          investigate = true,
          impact = true
        )
      )
    }
  }
}
