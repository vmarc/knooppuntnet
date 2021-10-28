package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.NetworkChanges
import kpn.api.common.NodeName
import kpn.api.common.changes.ChangeAction
import kpn.api.common.common.Ref
import kpn.api.common.diff.RefDiffs
import kpn.api.common.diff.TagDetail
import kpn.api.common.diff.TagDetailType
import kpn.api.common.diff.TagDiffs
import kpn.api.custom.ChangeType
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.doc.Label
import kpn.core.test.OverpassData

class NetworkUpdateNodeTest06 extends IntegrationTest {

  test("network update - removed node that looses required tags, but still has tags of other networkType does not become inactive") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .node(
        1002,
        tags = Tags.from(
          "network:type" -> "node_network",
          "rwn_ref" -> "02",
          "rcn_ref" -> "03"
        )
      )
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
      .node(
        1002,
        tags = Tags.from(
          "network:type" -> "node_network",
          "rcn_ref" -> "03"
        )
      )
      .networkRelation(
        1,
        "name",
        Seq(
          newMember("node", 1001)
          // node 02 no longer part of the network
        )
      )

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Modify, dataAfter.rawRelationWithId(1))

      assert(watched.nodes.contains(1001))
      assert(watched.networks.contains(1))

      // TODO assert(database.orphanNodes.isEmpty)

      //  findOrphanNodeById("nl:hiking:1001") should matchTo(
      //    newOrphanNodeDoc(
      //      country = Country.nl,
      //      networkType = NetworkType.hiking,
      //      nodeId = 1001L,
      //      name = "01"
      //    )
      //  )

      assertNetwork()
      assertNetworkInfo()

      assertNode()
      assertNetworkInfoChange()
      assertNodeChange()
      assertChangeSetSummary()
    }
  }

  private def assertNetwork(): Unit = {
    val networkDoc = findNetworkById(1)
    networkDoc._id should equal(1)
  }

  private def assertNetworkInfo(): Unit = {
    val networkInfoDoc = findNetworkInfoById(1)
    networkInfoDoc._id should equal(1)
  }

  private def assertNode(): Unit = {
    findNodeById(1002) should matchTo(
      newNodeDoc(
        1002,
        labels = Seq(
          Label.active,
          Label.networkType(NetworkType.cycling)
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
          "network:type" -> "node_network",
          "rcn_ref" -> "03",
        )
      )
    )
  }

  private def assertNetworkInfoChange(): Unit = {
    findNetworkInfoChangeById("123:1:1") should matchTo(
      newNetworkInfoChange(
        newChangeKey(elementId = 1),
        ChangeType.Update,
        Some(Country.nl),
        NetworkType.hiking,
        1,
        "name",
        networkDataUpdate = None,
        nodeDiffs = RefDiffs(
          removed = Seq(Ref(1002, "02"))
        ),
        investigate = true
      )
    )
  }

  private def assertNodeChange(): Unit = {
    findNodeChangeById("123:1:1002") should matchTo(
      newNodeChange(
        key = newChangeKey(elementId = 1002),
        changeType = ChangeType.Update,
        subsets = Seq(
          Subset.nlHiking,
          Subset.nlBicycle
        ),
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
        removedFromNetwork = Seq(
          Ref(1, "name")
        ),
        facts = Seq(
          Fact.LostHikingNodeTag
        ),
        investigate = true,
        impact = true,
        locationInvestigate = true,
        locationImpact = true
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
        nodeChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.nlBicycle,
            ChangeSetElementRefs(
              updated = Seq(
                newChangeSetElementRef(1002, "03", investigate = true)
              )
            )
          ),
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs(
              updated = Seq(
                newChangeSetElementRef(1002, "03", investigate = true)
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
