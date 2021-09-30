package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.common.Ref
import kpn.api.common.diff.RefDiffs
import kpn.api.common.diff.TagDetail
import kpn.api.common.diff.TagDetailType.Delete
import kpn.api.common.diff.TagDetailType.Same
import kpn.api.common.diff.TagDiffs
import kpn.api.custom.Country
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.doc.Label
import kpn.core.test.OverpassData

class NetworkDeleteNodeTest05 extends IntegrationTest {

  test("network delete - lost hiking node tag, but still retain bicyle node tag and become orphan") {

    val dataBefore = OverpassData()
      .node(
        1001,
        version = 1,
        tags = Tags.from(
          "network:type" -> "node_network",
          "rwn_ref" -> "01",
          "rcn_ref" -> "02"
        )
      )
      .networkRelation(
        1,
        "network",
        Seq(
          newMember("node", 1001)
        )
      )

    val dataAfter = OverpassData()
      .node(
        1001,
        version = 2,
        tags = Tags.from(
          "network:type" -> "node_network",
          "rcn_ref" -> "02"
        )
      )

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Delete, newRawRelation(1))

      assert(!watched.networks.contains(1))
      assert(watched.nodes.contains(1001))

      assertNode()
      assertNetwork()
      assertNetworkInfo()
      assertNetworkInfoChange()
      assertNodeChange()
      assertChangeSetSummary()
    }
  }

  private def assertNode(): Unit = {
    findNodeById(1001) should matchTo(
      newNodeDoc(
        1001,
        labels = Seq(
          Label.active,
          Label.networkType(NetworkType.cycling)
        ),
        country = Some(Country.nl),
        name = "02",
        names = Seq(
          newNodeName(NetworkType.cycling, NetworkScope.regional, "02")
        ),
        version = 2, // <--
        tags = Tags.from(
          "network:type" -> "node_network",
          "rcn_ref" -> "02"
        )
      )
    )
  }

  private def assertNetwork(): Unit = {
    val networkDoc = findNetworkById(1)
    networkDoc._id should equal(1)
  }

  private def assertNetworkInfo(): Unit = {
    findNetworkInfoById(1) should matchTo(
      newNetworkInfoDoc(
        1,
        active = false, // <--- !!!
        country = Some(Country.nl),
        newNetworkSummary(
          name = "network",
          networkType = NetworkType.hiking,
        ),
        newNetworkDetail(
          lastUpdated = defaultTimestamp, // TODO timestampAfterValue,
          relationLastUpdated = defaultTimestamp, // TODO timestampAfterValue
          tags = newNetworkTags("network")
        )
      )
    )
  }

  private def assertNetworkInfoChange(): Unit = {
    findNetworkInfoChangeById("123:1:1") should matchTo(
      newNetworkInfoChange(
        newChangeKey(elementId = 1),
        ChangeType.Delete,
        Some(Country.nl),
        NetworkType.hiking,
        1,
        "network",
        networkNodes = RefDiffs(
          removed = Seq(
            Ref(1001, "01")
          )
        ),
        investigate = true
      )
    )
  }

  private def assertNodeChange(): Unit = {
    findNodeChangeById("123:1:1001") should matchTo(
      newNodeChange(
        key = newChangeKey(elementId = 1001),
        changeType = ChangeType.Update,
        subsets = Seq(
          Subset.nlHiking,
          Subset.nlBicycle
        ),
        name = "02",
        before = Some(
          newMetaData(version = 1)
        ),
        after = Some(
          newMetaData(version = 2)
        ),
        tagDiffs = Some(
          TagDiffs(
            mainTags = Seq(
              TagDetail(Delete, "rwn_ref", Some("01"), None),
              TagDetail(Same, "rcn_ref", Some("02"), Some("02")),
              TagDetail(Same, "network:type", Some("node_network"), Some("node_network"))
            )
          )
        ),
        removedFromNetwork = Seq(
          Ref(1, "network")
        ),
        // TODO MONGO facts = Seq(Fact.LostHikingNodeTag),
        investigate = true,
        impact = true,
        // TODO MONGO locationInvestigate = true,
        // TODO MONGO locationImpact = true
      )
    )
  }

  private def assertChangeSetSummary(): Unit = {
    findChangeSetSummaryById("123:1") should matchTo(
      newChangeSetSummary(
        subsets = Seq(
          Subset.nlBicycle,
          Subset.nlHiking
        ),
        networkChanges = NetworkChanges(
          deletes = Seq(
            newChangeSetNetwork(
              Some(Country.nl),
              NetworkType.hiking,
              1,
              "network",
              nodeChanges = ChangeSetElementRefs(
                removed = Seq(
                  newChangeSetElementRef(1001, "01", investigate = true)
                )
              ),
              investigate = true
            )
          )
        ),
        nodeChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.nlBicycle,
            ChangeSetElementRefs(
              updated = Seq(
                newChangeSetElementRef(1001, "02", investigate = true)
              )
            )
          ),
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs(
              updated = Seq(
                newChangeSetElementRef(1001, "02", investigate = true)
              )
            )
          )
        ),
        subsetAnalyses = Seq(
          ChangeSetSubsetAnalysis(Subset.nlBicycle, investigate = true),
          ChangeSetSubsetAnalysis(Subset.nlHiking, investigate = true)

        ),
        investigate = true
      )
    )
  }
}
