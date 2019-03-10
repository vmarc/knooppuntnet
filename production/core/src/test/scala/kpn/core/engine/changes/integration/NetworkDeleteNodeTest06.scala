package kpn.core.engine.changes.integration

import kpn.core.changes.RelationAnalyzer
import kpn.core.test.TestData2
import kpn.shared.ChangeSetSummary
import kpn.shared.Country
import kpn.shared.Fact
import kpn.shared.LatLonImpl
import kpn.shared.NetworkChanges
import kpn.shared.NetworkType
import kpn.shared.Subset
import kpn.shared.changes.ChangeAction
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NetworkChange
import kpn.shared.changes.details.NodeChange
import kpn.shared.changes.details.RefChanges
import kpn.shared.common.Ref
import kpn.shared.data.Tags
import kpn.shared.diff.TagDetail
import kpn.shared.diff.TagDetailType.Add
import kpn.shared.diff.TagDetailType.Same
import kpn.shared.diff.TagDiffs
import kpn.shared.diff.node.NodeMoved
import kpn.shared.network.NetworkInfo

class NetworkDeleteNodeTest06 extends AbstractTest {

  test("network delete - node becomes ignored") {

    val dataBefore = TestData2()
      .networkNode(1001, "01")
      .networkRelation(1, "network1", Seq(newMember("node", 1001)))
      .data

    val dataAfter = TestData2()
      .foreignNetworkNode(1001, "01", extraTags = Tags.from("tag" -> "after"))
      .data

    val tc = new TestConfig()
    tc.relationBefore(dataBefore, 1)
    tc.nodesAfter(dataAfter, 1001)

    tc.analysisData.networks.watched.add(1, RelationAnalyzer.toElementIds(dataBefore.relations(1)))

    tc.process(ChangeAction.Delete, newRawRelation(1))

    tc.analysisData.networks.watched.contains(1) should equal(false)
    tc.analysisData.orphanNodes.watched.contains(1001) should equal(false)
    tc.analysisData.orphanNodes.ignored.contains(1001) should equal(true)

    (tc.networkRepository.save _).verify(
      where { networkInfo: NetworkInfo =>
        networkInfo should equal(
          newNetworkInfo(
            newNetworkAttributes(
              1,
              Some(Country.nl),
              NetworkType.hiking,
              "network1",
              lastUpdated = timestampAfterValue,
              relationLastUpdated = timestampAfterValue
            ),
            active = false // <--- !!!
          )
        )
        true
      }
    )

    (tc.analysisRepository.saveNode _).verify(*).once() // TODO CHANGE work out further details ?

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
                  "network1",
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

    (tc.changeSetRepository.saveNetworkChange _).verify(
      where { networkChange: NetworkChange =>
        networkChange should equal(
          newNetworkChange(
            newChangeKey(elementId = 1),
            ChangeType.Delete,
            Some(Country.nl),
            NetworkType.hiking,
            1,
            "network1",
            ignoredNodes = RefChanges(newRefs = Seq(Ref(1001, "01"))),
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
              newRawNode(
                1001,
                latitude = "99",
                longitude = "99",
                tags = Tags.from(
                  "rwn_ref" -> "01",
                  "tag" -> "after"
                )
              )
            ),
            tagDiffs = Some(
              TagDiffs(
                mainTags = Seq(
                  TagDetail(Same, "rwn_ref", Some("01"), Some("01"))
                ),
                extraTags = Seq(
                  TagDetail(Add, "tag", None, Some("after"))
                )
              )
            ),
            nodeMoved = Some(
              NodeMoved(
                LatLonImpl("0", "0"),
                LatLonImpl("99", "99"),
                9854401
              )
            ),
            removedFromNetwork = Seq(
              Ref(1, "network1")
            ),
            facts = Seq(Fact.BecomeIgnored)
          )
        )
        true
      }
    )
  }
}
