package kpn.core.engine.changes.integration

import kpn.core.changes.RelationAnalyzer
import kpn.core.test.TestData2
import kpn.shared.ChangeSetSummary
import kpn.shared.Country
import kpn.shared.Fact
import kpn.shared.NetworkChanges
import kpn.shared.NetworkType
import kpn.shared.Subset
import kpn.shared.changes.ChangeAction
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NetworkChange
import kpn.shared.changes.details.NodeChange
import kpn.shared.common.Ref
import kpn.shared.diff.TagDetail
import kpn.shared.diff.TagDetailType.Delete
import kpn.shared.diff.TagDiffs
import kpn.shared.network.NetworkInfo

class NetworkDeleteNodeTest04 extends AbstractTest {

  test("network delete - node looses node tag") {

    val dataBefore = TestData2()
      .networkNode(1001, "01")
      .networkRelation(1, "network", Seq(newMember("node", 1001)))
      .data

    val dataAfter = TestData2()
      .node(1001)
      .data

    val tc = new TestConfig()
    tc.relationBefore(dataBefore, 1)
    tc.nodesAfter(dataAfter, 1001)

    tc.analysisData.networks.watched.add(1, RelationAnalyzer.toElementIds(dataBefore.relations(1)))

    tc.process(ChangeAction.Delete, newRawRelation(1))

    tc.analysisData.networks.watched.contains(1) should equal(false)

    (tc.networkRepository.save _).verify(
      where { networkInfo: NetworkInfo =>
        networkInfo should equal(
          newNetworkInfo(
            newNetworkAttributes(
              1,
              Some(Country.nl),
              NetworkType.hiking,
              "network",
              lastUpdated = timestampAfterValue,
              relationLastUpdated = timestampAfterValue
            ),
            active = false // <--- !!!
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
            networkChanges = NetworkChanges(
              deletes = Seq(
                newChangeSetNetwork(
                  Some(Country.nl),
                  NetworkType.hiking,
                  1,
                  "network",
                  investigate = true
                )
              )
            ),
            investigate = true
          )
        )
        true
      }
    )

    (tc.analysisRepository.saveNode _).verify(*).once()

    (tc.changeSetRepository.saveNetworkChange _).verify(
      where { networkChange: NetworkChange =>
        networkChange should equal(
          newNetworkChange(
            newChangeKey(elementId = 1),
            ChangeType.Delete,
            Some(Country.nl),
            NetworkType.hiking,
            1,
            "network",
            investigate = true
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveRouteChange _).verify(*).never()

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
                mainTags = Seq(
                  TagDetail(Delete, "rwn_ref", Some("01"), None)
                )
              )
            ),
            removedFromNetwork = Seq(
              Ref(1, "network")
            ),
            facts = Seq(Fact.LostHikingNodeTag)
          )
        )
        true
      }
    )
  }
}
