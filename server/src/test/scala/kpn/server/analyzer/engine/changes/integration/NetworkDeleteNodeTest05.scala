package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSummary
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.common.Ref
import kpn.api.common.diff.TagDetail
import kpn.api.common.diff.TagDetailType.Delete
import kpn.api.common.diff.TagDetailType.Same
import kpn.api.common.diff.TagDiffs
import kpn.api.common.network.NetworkInfo
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.test.TestData2

class NetworkDeleteNodeTest05 extends AbstractTest {

  test("network delete - lost hiking node tag, but still retain bicyle node tag and become orphan") {

    val dataBefore = TestData2()
      .node(1001, tags = Tags.from("rwn_ref" -> "01", "rcn_ref" -> "02", "network:type" -> "node_network"))
      .networkRelation(1, "network", Seq(newMember("node", 1001)))
      .data

    val dataAfter = TestData2()
      .node(1001, tags = Tags.from("rcn_ref" -> "02", "network:type" -> "node_network"))
      .data

    val tc = new TestConfig()
    tc.relationBefore(dataBefore, 1)
    tc.nodesAfter(dataAfter, 1001)

    tc.analysisContext.data.networks.watched.add(1, tc.relationAnalyzer.toElementIds(dataBefore.relations(1)))

    tc.process(ChangeAction.Delete, newRawRelation(1))

    tc.analysisContext.data.networks.watched.contains(1) should equal(false)
    // tc.analysisContext.data.orphanNodes.watched.contains(1001) should equal(true) TODO CHANGE !!!

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

    (tc.analysisRepository.saveNode _).verify(*).once()

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
      where { nodeChange: NodeChange =>
        nodeChange should equal(
          newNodeChange(
            key = newChangeKey(elementId = 1001),
            changeType = ChangeType.Update,
            subsets = Seq(Subset.nlHiking),
            name = "02",
            before = Some(
              newRawNode(1001, tags = Tags.from("rwn_ref" -> "01", "rcn_ref" -> "02", "network:type" -> "node_network"))
            ),
            after = Some(
              newRawNode(1001, tags = Tags.from("rcn_ref" -> "02", "network:type" -> "node_network"))
            ),
            tagDiffs = Some(
              TagDiffs(
                mainTags = Seq(
                  TagDetail(Same, "rcn_ref", Some("02"), Some("02")),
                  TagDetail(Delete, "rwn_ref", Some("01"), None),
                  TagDetail(Same, "network:type", Some("node_network"), Some("node_network"))
                )
              )
            ),
            removedFromNetwork = Seq(
              Ref(1, "network")
            ),
            facts = Seq(Fact.LostHikingNodeTag),
            investigate = true,
            locationInvestigate = true
          )
        )
        true
      }
    )
  }
}
