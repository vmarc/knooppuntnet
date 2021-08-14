package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.NetworkChanges
import kpn.api.common.NodeName
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.common.Ref
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.common.diff.TagDetail
import kpn.api.common.diff.TagDetailType
import kpn.api.common.diff.TagDiffs
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class NetworkUpdateNodeTest06 extends AbstractIntegrationTest {

  test("network update - removed node that looses required tags, but still has tags of other networkType does not become inactive") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .node(1002, tags = Tags.from("rwn_ref" -> "02", "rcn_ref" -> "03", "network:type" -> "node_network"))
      .networkRelation(
        1,
        "name",
        Seq(
          newMember("node", 1001),
          newMember("node", 1002)
        )
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .node(1002, tags = Tags.from("rcn_ref" -> "03", "network:type" -> "node_network"))
      .networkRelation(
        1,
        "name",
        Seq(
          newMember("node", 1001)
          // node 02 no longer part of the network
        )
      )

    withDatabase { database =>

      val tc = new IntegrationTestContext(database, dataBefore, dataAfter)

      tc.process(ChangeAction.Modify, dataAfter.rawRelationWithId(1))

      assert(!tc.analysisContext.watched.nodes.contains(1001))

      val networkDoc = tc.findNetworkById(1)
      networkDoc._id should equal(1)

      val networkInfoDoc = tc.findNetworkInfoById(1)
      networkInfoDoc._id should equal(1)

      assert(database.routes.isEmpty)

      tc.findNodeById(1002) should matchTo(
        newNodeDoc(
          1002,
          labels = Seq(
            "active",
            "network-type-cycling"
          ),
          country = Some(Country.nl),
          name = "03",
          names = Seq(
            NodeName(
              NetworkType.cycling,
              NetworkScope.regional,
              "03",
              None,
              proposed = false
            )
          ),
          tags = Tags.from(
            "rcn_ref" -> "03",
            "network:type" -> "node_network"
          )
        )
      )

      tc.findChangeSetSummaryById("123:1") should matchTo(
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

      tc.findNetworkInfoChangeById("123:1:1") should matchTo(
        newNetworkInfoChange(
          newChangeKey(elementId = 1),
          ChangeType.Update,
          Some(Country.nl),
          NetworkType.hiking,
          1,
          "name",
          networkDataUpdate = Some(
            NetworkDataUpdate(
              newNetworkData(name = "name"),
              newNetworkData(name = "name")
            )
          ),
          networkNodes = RefDiffs(
            removed = Seq(Ref(1002, "02"))
          ),
          investigate = true
        )
      )

      tc.findNodeChangeById("123:1:1001") should matchTo(
        newNodeChange(
          key = newChangeKey(elementId = 1002),
          changeType = ChangeType.Update,
          subsets = Seq(Subset.nlHiking),
          name = "03",
          before = Some(
            newMetaData()
          ),
          after = Some(
            newMetaData()
          ),
          tagDiffs = Some(
            TagDiffs(
              Seq(
                TagDetail(TagDetailType.Delete, "rwn_ref", Some("02"), None),
                TagDetail(TagDetailType.Same, "rcn_ref", Some("03"), Some("03")),
                TagDetail(TagDetailType.Same, "network:type", Some("node_network"), Some("node_network"))
              ),
              Seq.empty
            )
          ),
          removedFromNetwork = Seq(Ref(1, "name")),
          facts = Seq(Fact.LostHikingNodeTag),
          investigate = true,
          impact = true,
          locationInvestigate = true,
          locationImpact = true
        )
      )
    }
  }
}
