package kpn.server.analyzer.engine.changes.integration

import kpn.core.test.TestData2
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

class OrphanNodeTest01 extends AbstractTest {

  test("create orphan node") {

    val dataAfter = TestData2()
      .networkNode(1001, "01")
      .data

    val tc = new TestConfig()

    tc.process(ChangeAction.Create, node(dataAfter, 1001))

    tc.analysisContext.data.orphanNodes.watched.contains(1001) should equal(true)

    (tc.analysisRepository.saveNode _).verify(
      where { nodeInfo: NodeInfo =>
        nodeInfo should equal(
          NodeInfo(
            1001,
            active = true,
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
            newNodeTags("01"),
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
                  added = Seq(newChangeSetElementRef(1001, "01", happy = true))
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

    (tc.changeSetRepository.saveNodeChange _).verify(
      where { nodeChange: NodeChange =>
        nodeChange should equal(
          newNodeChange(
            newChangeKey(elementId = 1001),
            ChangeType.Create,
            Seq(Subset.nlHiking),
            "01",
            after = Some(
              newRawNodeWithName(1001, "01")
            ),
            facts = Seq(Fact.OrphanNode),
            happy = true
          )
        )
        true
      }
    )
  }
}
