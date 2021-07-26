package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSummary
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RefChanges
import kpn.api.common.common.Ref
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.test.TestData2

class NetworkUpdateNodeTest08 extends AbstractTest {

  test("network update - an orphan node that is added to the network is no longer orphan") {

    val dataBefore = TestData2()
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
      .data

    val dataAfter = TestData2()
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
      .data

    val tc = new TestConfig()
    tc.watchOrphanNode(1002)
    tc.relationBefore(dataBefore, 1)
    tc.watchNetwork(dataBefore, 1)
    tc.relationAfter(dataAfter, 1)
    tc.nodesBefore(dataBefore, 1002)

    // before:
    assert(!tc.analysisContext.data.networks.watched.isReferencingNode(1002))
    assert(tc.analysisContext.data.orphanNodes.watched.contains(1002))

    // act:
    tc.process(ChangeAction.Modify, relation(dataAfter, 1))

    // after:
    assert(tc.analysisContext.data.networks.watched.isReferencingNode(1002))
    assert(!tc.analysisContext.data.orphanNodes.watched.contains(1002))

    (tc.networkRepository.save _).verify(*).once()

    (tc.changeSetRepository.saveChangeSetSummary _).verify(
      where { changeSetSummary: ChangeSetSummary =>
        changeSetSummary should matchTo(
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
        true
      }
    )

    (tc.changeSetRepository.saveNetworkChange _).verify(
      where { networkChange: NetworkChange =>
        networkChange should matchTo(
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
        true
      }
    )

    (tc.changeSetRepository.saveNodeChange _).verify(
      where { nodeChange: NodeChange =>
        nodeChange should matchTo(
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
        true
      }
    )
  }

}
