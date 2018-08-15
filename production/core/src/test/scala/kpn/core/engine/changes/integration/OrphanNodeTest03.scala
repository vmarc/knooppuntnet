package kpn.core.engine.changes.integration

import kpn.core.test.TestData
import kpn.core.test.TestData2
import kpn.shared.ChangeSetElementRef
import kpn.shared.ChangeSetElementRefs
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
import kpn.shared.diff.common.FactDiffs

class OrphanNodeTest03 extends AbstractTest {

  test("delete orphan node") {

    val dataBefore = TestData2()
      .networkNode(1001, "01")
      .data

    val dataAfter = new TestData().data

    val tc = new TestConfig()

    tc.analysisData.orphanNodes.watched.add(1001)

    tc.nodesBefore(dataBefore, 1001)
    tc.nodeAfter(dataAfter, 1001)

    tc.process(ChangeAction.Delete, newRawNode(1001))

    tc.analysisData.orphanNodes.watched.contains(1001) should equal(false)
    tc.analysisData.orphanNodes.ignored.contains(1001) should equal(false)

    (tc.analysisRepository.saveNode _).verify(
      where { (nodeInfo: NodeInfo) =>
        nodeInfo should equal(
          NodeInfo(
            1001,
            active = false, // <-- !!
            display = true,
            ignored = false,
            orphan = true,
            Some(Country.nl),
            "01",
            "",
            "",
            "",
            "",
            "01",
            "0",
            "0",
            Timestamp(2015, 8, 11, 0, 0, 0),
            Tags.from("rwn_ref" -> "01"),
            Seq(Fact.Deleted)
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveChangeSetSummary _).verify(
      where { (changeSetSummary: ChangeSetSummary) =>
        changeSetSummary should equal(
          newChangeSetSummary(
            subsets = Seq(Subset.nlHiking),
            orphanNodeChanges = Seq(
              ChangeSetSubsetElementRefs(
                Subset.nlHiking,
                ChangeSetElementRefs(
                  removed = Seq(
                    newChangeSetElementRef(1001, "01", investigate = true)
                  )
                )
              )
            ),
            investigate = true
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveNodeChange _).verify(
      where { (nodeChange: NodeChange) =>
        nodeChange should equal(
          newNodeChange(
            newChangeKey(elementId = 1001),
            ChangeType.Delete,
            Seq(Subset.nlHiking),
            "01",
            before = Some(
              newRawNodeWithName(1001, "01")
            ),
            facts = Seq(Fact.WasOrphan, Fact.Deleted)
          )
        )
        true
      }
    )
  }
}
