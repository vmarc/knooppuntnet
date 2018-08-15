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
import kpn.shared.data.Tags
import kpn.shared.diff.TagDetail
import kpn.shared.diff.TagDetailType.Delete
import kpn.shared.diff.TagDetailType.Same
import kpn.shared.diff.TagDiffs
import kpn.shared.network.NetworkInfo

class NetworkDeleteNodeTest05 extends AbstractTest {

  test("network delete - lost hiking node tag, but still retain bicyle node tag and become orphan") {

    val dataBefore = TestData2()
      .node(1001, tags = Tags.from("rwn_ref" -> "01", "rcn_ref" -> "02"))
      .networkRelation(1, "network", Seq(newMember("node", 1001)))
      .data

    val dataAfter = TestData2()
      .node(1001, tags = Tags.from("rcn_ref" -> "02"))
      .data

    val tc = new TestConfig()
    tc.relationBefore(dataBefore, 1)
    tc.nodesAfter(dataAfter, 1001)

    tc.analysisData.networks.watched.add(1, RelationAnalyzer.toElementIds(dataBefore.relations(1)))

    tc.process(ChangeAction.Delete, newRawRelation(1))

    tc.analysisData.networks.watched.contains(1) should equal(false)
    // tc.analysisData.orphanNodes.watched.contains(1001) should equal(true) TODO CHANGE !!!

    (tc.networkRepository.save _).verify(
      where { (networkInfo: NetworkInfo) =>
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

    (tc.analysisRepository.saveNode _).verify(*).once()

    (tc.changeSetRepository.saveChangeSetSummary _).verify(
      where { (changeSetSummary: ChangeSetSummary) =>
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

    (tc.changeSetRepository.saveNetworkChange _).verify(
      where { (networkChange: NetworkChange) =>
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

    (tc.changeSetRepository.saveNodeChange _).verify(
      where { (nodeChange: NodeChange) =>
        nodeChange should equal(
          newNodeChange(
            newChangeKey(elementId = 1001),
            ChangeType.Update,
            Seq(Subset.nlHiking),
            "02",
            before = Some(
              newRawNode(1001, tags = Tags.from("rwn_ref" -> "01", "rcn_ref" -> "02"))
            ),
            after = Some(
              newRawNode(1001, tags = Tags.from("rcn_ref" -> "02"))
            ),
            tagDiffs = Some(
              TagDiffs(
                mainTags = Seq(
                  TagDetail(Same, "rcn_ref", Some("02"), Some("02")),
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
