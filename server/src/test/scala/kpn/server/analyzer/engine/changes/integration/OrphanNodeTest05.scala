package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.ChangeSetSummary
import kpn.api.common.NodeInfo
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.diff.TagDetail
import kpn.api.common.diff.TagDetailType
import kpn.api.common.diff.TagDiffs
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.test.TestData2

class OrphanNodeTest05 extends AbstractTest {

  test("orphan node looses node tag") {

    val dataBefore = TestData2()
      .networkNode(1001, "01")
      .data

    val dataAfter = TestData2()
      .node(1001) // rwn_ref tag no longer available, but node still exists
      .data

    val tc = new TestConfig()

    tc.analysisContext.data.orphanNodes.watched.add(1001)

    tc.nodesBefore(dataBefore, 1001)
    tc.nodeAfter(dataAfter, 1001)

    tc.process(ChangeAction.Modify, node(dataAfter, 1001))

    assert(!tc.analysisContext.data.orphanNodes.watched.contains(1001))

    (tc.nodeRepository.save _).verify(
      where { nodeInfo: NodeInfo =>
        nodeInfo should matchTo(
          NodeInfo(
            1001,
            1001,
            labels = Seq(
              "orphan"
            ),
            active = false, // <-- !!
            orphan = true,
            Some(Country.nl),
            "",
            Seq.empty,
            "0",
            "0",
            Timestamp(2015, 8, 11, 0, 0, 0),
            None,
            Tags.empty,
            Seq.empty,
            Seq.empty,
            Seq.empty,
            None,
            Seq.empty
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveChangeSetSummary _).verify(
      where { changeSetSummary: ChangeSetSummary =>
        changeSetSummary should matchTo(
          newChangeSetSummary(
            subsets = Seq(Subset.nlHiking),
            orphanNodeChanges = Seq(
              ChangeSetSubsetElementRefs(
                Subset.nlHiking,
                ChangeSetElementRefs(
                  updated = Seq(
                    ChangeSetElementRef(1001, "01", happy = false, investigate = true)
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
        true
      }
    )

    (tc.changeSetRepository.saveNodeChange _).verify(
      where { nodeChange: NodeChange =>
        nodeChange should matchTo(
          newNodeChange(
            key = newChangeKey(elementId = 1001),
            changeType = ChangeType.Update,
            subsets = Seq(Subset.nlHiking),
            name = "01",
            before = Some(
              newRawNodeWithName(1001, "01")
            ),
            after = Some(
              newRawNode(1001)
            ),
            tagDiffs = Some(
              TagDiffs(
                Seq(
                  TagDetail(TagDetailType.Delete, "rwn_ref", Some("01"), None),
                  TagDetail(TagDetailType.Delete, "network:type", Some("node_network"), None)
                )
              )
            ),
            facts = Seq(Fact.WasOrphan, Fact.LostHikingNodeTag),
            investigate = true,
            impact = true,
            locationInvestigate = true,
            locationImpact = true
          )
        )
        true
      }
    )
  }
}
