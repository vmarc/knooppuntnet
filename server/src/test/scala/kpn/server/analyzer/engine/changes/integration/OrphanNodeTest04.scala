package kpn.server.analyzer.engine.changes.integration

import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.test.TestData2
import kpn.api.common.NodeInfo
import kpn.api.common.changes.ChangeAction

class OrphanNodeTest04 extends AbstractTest {

  test("delete orphan node, and 'before' situation cannot be found in overpass database") {

    val noData = TestData2().data

    val tc = new TestConfig()

    tc.analysisContext.data.orphanNodes.watched.add(1001)

    tc.nodesBefore(noData, 1001)
    tc.nodeAfter(noData, 1001)

    tc.process(ChangeAction.Delete, newRawNode(1001))

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
            Seq(),
            "0",
            "0",
            Timestamp(2015, 8, 11, 0, 0, 0),
            None,
            Tags.empty,
            Seq(Fact.Deleted),
            None,
            Seq()
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveChangeSetSummary _).verify(*).never()
    (tc.changeSetRepository.saveNodeChange _).verify(*).never()
  }
}
