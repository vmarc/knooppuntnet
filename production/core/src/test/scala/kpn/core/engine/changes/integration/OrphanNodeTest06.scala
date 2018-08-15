package kpn.core.engine.changes.integration

import kpn.core.test.TestData2
import kpn.shared.Fact
import kpn.shared.NodeInfo
import kpn.shared.Timestamp
import kpn.shared.changes.ChangeAction
import kpn.shared.data.Tags

class OrphanNodeTest06 extends AbstractTest {

  test("create ignored orphan node") {

    val dataAfter = TestData2()
      .foreignNetworkNode(1001, "01")
      .data

    val tc = new TestConfig()

    tc.process(ChangeAction.Create, node(dataAfter, 1001))

    tc.analysisData.orphanNodes.watched.contains(1001) should equal(false)
    tc.analysisData.orphanNodes.ignored.contains(1001) should equal(true)

    (tc.analysisRepository.saveNode _).verify(
      where { (nodeInfo: NodeInfo) =>
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
            "",
            "",
            "",
            "01",
            "99",
            "99",
            Timestamp(2015, 8, 11, 0, 0, 0),
            Tags.from("rwn_ref" -> "01"),
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
