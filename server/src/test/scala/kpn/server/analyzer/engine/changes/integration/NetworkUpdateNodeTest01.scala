package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSummary
import kpn.api.common.NetworkChanges
import kpn.api.common.NodeInfo
import kpn.api.common.NodeName
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RefChanges
import kpn.api.common.common.Ref
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.test.TestData2

class NetworkUpdateNodeTest01 extends AbstractTest {

  test("network update - node that is no longer part of the network after update, becomes orphan node if also not referenced in any other network or orphan route") {

    val dataBefore = TestData2()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .networkRelation(
        1,
        "name",
        Seq(
          newMember("node", 1001),
          newMember("node", 1002)
        )
      )
      .data

    val dataAfter = TestData2()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .networkRelation(
        1,
        "name",
        Seq(
          newMember("node", 1001)
          // node 02 no longer part of the network
        )
      )
      .data

    val tc = new TestConfig()
    tc.relationBefore(dataBefore, 1)
    tc.watchNetwork(dataBefore, 1)
    tc.relationAfter(dataAfter, 1)
    tc.nodeAfter(dataAfter, 1002)
    tc.nodesAfter(dataAfter, 1002)

    // before:
    assert(tc.analysisContext.data.networks.watched.isReferencingNode(1002))
    assert(!tc.analysisContext.data.orphanNodes.watched.contains(1002))

    // act:
    tc.process(ChangeAction.Modify, relation(dataAfter, 1))

    // after:
    assert(!tc.analysisContext.data.networks.watched.isReferencingNode(1002))
    assert(tc.analysisContext.data.orphanNodes.watched.contains(1002))

    (tc.networkRepository.save _).verify(*).once()
    (tc.routeRepository.save _).verify(*).never()
    (tc.nodeRepository.save _).verify(
      where { nodeInfo: NodeInfo =>
        nodeInfo.copy(tiles = Seq.empty) should matchTo(
          newNodeInfo(
            1002,
            labels = Seq(
              "active",
              "orphan",
              "network-type-hiking"
            ),
            orphan = true,
            country = Some(Country.nl),
            name = "02",
            names = Seq(
              NodeName(
                NetworkType.hiking,
                NetworkScope.regional,
                "02",
                None,
                proposed = false
              )
            ),
            tags = newNodeTags("02")
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
            networkChanges = NetworkChanges(
              updates = Seq(
                newChangeSetNetwork(
                  Some(Country.nl),
                  NetworkType.hiking,
                  1,
                  "name",
                  nodeChanges = ChangeSetElementRefs(
                    removed = Seq(
                      newChangeSetElementRef(1002, "02", investigate = true)
                    )
                  ),
                  investigate = true
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


    (tc.changeSetRepository.saveNetworkChange _).verify(
      where { networkChange: NetworkChange =>
        networkChange should matchTo(
          newNetworkChange(
            newChangeKey(elementId = 1),
            ChangeType.Update,
            Some(Country.nl),
            NetworkType.hiking,
            1,
            "name",
            orphanNodes = RefChanges(
              newRefs = Seq(
                Ref(1002, "02")
              )
            ),
            networkDataUpdate = Some(
              NetworkDataUpdate(
                newNetworkData(name = "name"),
                newNetworkData(name = "name")
              )
            ),
            networkNodes = RefDiffs(removed = Seq(Ref(1002, "02"))),
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
            key = newChangeKey(elementId = 1002),
            changeType = ChangeType.Update,
            subsets = Seq(Subset.nlHiking),
            name = "02",
            before = Some(
              newRawNodeWithName(1002, "02")
            ),
            after = Some(
              newRawNodeWithName(1002, "02")
            ),
            removedFromNetwork = Seq(Ref(1, "name")),
            facts = Seq(Fact.BecomeOrphan),
            investigate = true,
            impact = true
          )
        )
        true
      }
    )
  }
}
