package kpn.core.engine.changes.integration

import kpn.core.test.TestData2
import kpn.shared.Country
import kpn.shared.Fact
import kpn.shared.NodeInfo
import kpn.shared.Timestamp
import kpn.shared.changes.ChangeAction
import kpn.shared.data.Tags

class OrphanNodeTest04 extends AbstractTest {

  test("delete orphan node, and 'before' situation cannot be found in overpass database") {

    val noData = TestData2().data

    val tc = new TestConfig()

    tc.analysisData.orphanNodes.watched.add(1001)

    tc.nodesBefore(noData, 1001)
    tc.nodeAfter(noData, 1001)

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
            Seq(Fact.Deleted)
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveChangeSetSummary _).verify(*).never()
    (tc.changeSetRepository.saveNodeChange _).verify(*).never()
  }
}
