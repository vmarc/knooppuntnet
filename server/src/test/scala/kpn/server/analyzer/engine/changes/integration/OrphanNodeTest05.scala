package kpn.server.analyzer.engine.changes.integration

import kpn.core.test.TestData2
import kpn.shared.ChangeSetElementRef
import kpn.shared.ChangeSetElementRefs
import kpn.shared.ChangeSetSubsetAnalysis
import kpn.shared.ChangeSetSubsetElementRefs
import kpn.shared.ChangeSetSummary
import kpn.shared.Country
import kpn.shared.Fact
import kpn.shared.NodeInfo
import kpn.shared.Subset
import kpn.shared.Timestamp
import kpn.shared.changes.ChangeAction
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NodeChange
import kpn.shared.data.Tags
import kpn.shared.diff.TagDetail
import kpn.shared.diff.TagDetailType
import kpn.shared.diff.TagDiffs

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

    tc.analysisContext.data.orphanNodes.watched.contains(1001) should equal(false)

    (tc.analysisRepository.saveNode _).verify(
      where { nodeInfo: NodeInfo =>
        nodeInfo should equal(
          NodeInfo(
            1001,
            active = false, // <-- !!
            orphan = true,
            Some(Country.nl),
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "0",
            "0",
            Timestamp(2015, 8, 11, 0, 0, 0),
            Tags.empty,
            Seq(),
            None
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveChangeSetSummary _).verify(
      where { changeSetSummary: ChangeSetSummary =>
        changeSetSummary should equal(
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
        nodeChange should equal(
          newNodeChange(
            newChangeKey(elementId = 1001),
            ChangeType.Update,
            Seq(Subset.nlHiking),
            "01",
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
            investigate = true
          )
        )
        true
      }
    )
  }
}
