package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.ChangeSetSummary
import kpn.api.common.NodeInfo
import kpn.api.common.NodeName
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NodeChange
import kpn.api.custom.{Country, Fact, ScopedNetworkType, Subset, Tags, Timestamp}
import kpn.core.test.TestData2

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
        nodeInfo.copy(tiles = Seq()) should equal(
          NodeInfo(
            1001,
            active = true,
            orphan = true,
            Some(Country.nl),
            "01",
            Seq(
              NodeName(
                ScopedNetworkType.rwn,
                "01",
                proposed = false
              )
            ),
            "0",
            "0",
            Timestamp(2015, 8, 11, 0, 0, 0),
            lastSurvey = None,
            newNodeTags("01"),
            Seq(),
            None,
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
            key = newChangeKey(elementId = 1001),
            changeType = ChangeType.Create,
            subsets = Seq(Subset.nlHiking),
            name = "01",
            after = Some(
              newRawNodeWithName(1001, "01")
            ),
            facts = Seq(Fact.OrphanNode),
            happy = true,
            locationHappy = true
          )
        )
        true
      }
    )
  }

  test("create proposed orphan node") {

    val dataAfter = TestData2()
      .node(1001, tags = Tags.from("proposed:rwn_ref" -> "01", "network:type" -> "node_network"))
      .data

    val tc = new TestConfig()

    tc.process(ChangeAction.Create, node(dataAfter, 1001))

    tc.analysisContext.data.orphanNodes.watched.contains(1001) should equal(true)

    (tc.analysisRepository.saveNode _).verify(
      where { nodeInfo: NodeInfo =>
        nodeInfo.copy(tiles = Seq()) should equal(
          NodeInfo(
            1001,
            active = true,
            orphan = true,
            Some(Country.nl),
            "01",
            Seq(
              NodeName(
                ScopedNetworkType.rwn,
                "01",
                proposed = true
              )
            ),
            "0",
            "0",
            Timestamp(2015, 8, 11, 0, 0, 0),
            lastSurvey = None,
            Tags.from(
              "proposed:rwn_ref" -> "01",
              "network:type" -> "node_network"
            ),
            Seq(),
            None,
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
            key = newChangeKey(elementId = 1001),
            changeType = ChangeType.Create,
            subsets = Seq(Subset.nlHiking),
            name = "01",
            after = Some(
              newRawNode(
                1001,
                tags = Tags.from(
                  "proposed:rwn_ref" -> "01",
                  "network:type" -> "node_network"
                )
              )
            ),
            facts = Seq(Fact.OrphanNode),
            happy = true,
            locationHappy = true
          )
        )
        true
      }
    )
  }
}
