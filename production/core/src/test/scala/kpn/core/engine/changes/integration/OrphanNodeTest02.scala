package kpn.core.engine.changes.integration

import kpn.core.test.TestData2
import kpn.shared.ChangeSetElementRef
import kpn.shared.ChangeSetElementRefs
import kpn.shared.ChangeSetSubsetAnalysis
import kpn.shared.ChangeSetSubsetElementRefs
import kpn.shared.ChangeSetSummary
import kpn.shared.Country
import kpn.shared.Fact
import kpn.shared.NetworkChanges
import kpn.shared.NodeInfo
import kpn.shared.Subset
import kpn.shared.Timestamp
import kpn.shared.changes.ChangeAction
import kpn.shared.changes.details.ChangeKey
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NodeChange
import kpn.shared.data.Tags
import kpn.shared.diff.TagDetail
import kpn.shared.diff.TagDetailType
import kpn.shared.diff.TagDiffs
import kpn.shared.diff.common.FactDiffs

class OrphanNodeTest02 extends AbstractTest {

  test("update orphan node") {

    val dataBefore = TestData2()
      .networkNode(1001, "01", extraTags = Tags.from("tag" -> "before"))
      .data

    val dataAfter = TestData2()
      .networkNode(1001, "01", extraTags = Tags.from("tag" -> "after"))
      .data

    val tc = new TestConfig()

    tc.analysisData.orphanNodes.watched.add(1001)

    tc.nodesBefore(dataBefore, 1001)
    tc.nodeAfter(dataAfter, 1001)

    tc.process(ChangeAction.Modify, node(dataAfter, 1001))

    tc.analysisData.orphanNodes.watched.contains(1001) should equal(true)
    tc.analysisData.orphanNodes.ignored.contains(1001) should equal(false)

    (tc.analysisRepository.saveNode _).verify(
      where { nodeInfo: NodeInfo =>
        nodeInfo should equal(
          NodeInfo(
            1001,
            active = true,
            display = true,
            ignored = false,
            orphan = true,
            Some(Country.nl),
            "01",
            "",
            "01",
            "",
            "",
            "",
            "",
            "0",
            "0",
            Timestamp(2015, 8, 11, 0, 0, 0),
            Tags.from("rwn_ref" -> "01", "tag" -> "after"),
            Seq()
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
                    newChangeSetElementRef(1001, "01")
                  )
                )
              )
            ),
            subsetAnalyses = Seq(
              ChangeSetSubsetAnalysis(Subset.nlHiking)
            )
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
              newRawNode(
                1001,
                tags = Tags.from("rwn_ref" -> "01", "tag" -> "before")
              )
            ),
            after = Some(
              newRawNode(
                1001,
                tags = Tags.from("rwn_ref" -> "01", "tag" -> "after")
              )
            ),
            tagDiffs = Some(
              TagDiffs(
                mainTags = Seq(
                  TagDetail(TagDetailType.Same, "rwn_ref", Some("01"), Some("01"))
                ),
                extraTags = Seq(
                  TagDetail(TagDetailType.Update, "tag", Some("before"), Some("after"))
                )
              )
            ),
            facts = Seq(Fact.OrphanNode)
          )
        )
        true
      }
    )
  }
}
