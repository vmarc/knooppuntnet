package kpn.core.engine.changes.integration

import kpn.core.test.TestData2
import kpn.shared.Fact
import kpn.shared.NodeInfo
import kpn.shared.Timestamp
import kpn.shared.changes.ChangeAction
import kpn.shared.data.Tags

class OrphanNodeTest07 extends AbstractTest {

  test("update ignored orphan node") {

    val dataBefore = TestData2()
      .foreignNetworkNode(1001, "01", extraTags = Tags.from("tag" -> "before"))
      .data

    val dataAfter = TestData2()
      .foreignNetworkNode(1001, "01", extraTags = Tags.from("tag" -> "after"))
      .data

    val tc = new TestConfig()

    tc.analysisData.orphanNodes.ignored.add(1001)

    tc.nodesBefore(dataBefore, 1001)
    tc.nodeAfter(dataAfter, 1001)

    tc.process(ChangeAction.Modify, node(dataAfter, 1001))

    tc.analysisData.orphanNodes.watched.contains(1001) should equal(false)
    tc.analysisData.orphanNodes.ignored.contains(1001) should equal(true)

    (tc.analysisRepository.saveNode _).verify(
      where { nodeInfo: NodeInfo =>
        nodeInfo should equal(
          NodeInfo(
            1001,
            active = true,
            display = true,
            ignored = true,
            orphan = true,
            None,
            "01",
            "",
            "01",
            "",
            "",
            "",
            "",
            "99",
            "99",
            Timestamp(2015, 8, 11, 0, 0, 0),
            Tags.from("rwn_ref" -> "01", "tag" -> "after"),
            Seq(Fact.IgnoreForeignCountry)
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveChangeSetSummary _).verify(*).never()
    (tc.changeSetRepository.saveNodeChange _).verify(*).never()
  }
}
