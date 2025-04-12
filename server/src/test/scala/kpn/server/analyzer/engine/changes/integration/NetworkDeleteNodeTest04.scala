package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.NetworkChanges
import kpn.api.common.RouteType
import kpn.api.common.changes.ChangeAction
import kpn.api.common.common.Ref
import kpn.api.common.data.MemberType
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.api.common.diff.TagDetail
import kpn.api.common.diff.TagDetailType
import kpn.api.common.diff.TagDiffs
import kpn.api.custom.Change
import kpn.api.custom.Subset
import kpn.core.test.OverpassData

class NetworkDeleteNodeTest04 extends IntegrationTest {

  test("network delete - node looses node tag") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01", version = 1) // change
      .networkRelation( // delete
        1,
        "network",
        Seq(
          newMember(MemberType.Node, 1001)
        )
      )

    val dataAfter = OverpassData()
      .node(1001, version = 2) // change

    testIntegration(dataBefore, dataAfter) {
      process(
        Seq(
          Change(ChangeAction.Modify, Seq(newRawNode(1001))),
          Change(ChangeAction.Delete, Seq(newRawRelation(1)))
        )
      )

      database.orphanNodes shouldBe empty // the node does not become orphan, it is no longer a network node

      watched.networks.ids shouldNot contain(1)
      watched.nodes.ids shouldNot contain(1001)

      assertBaseNode()
      assertNode()
      assertNetwork()
      assertNetworkChange()
      assertNodeChange()
      assertChangeSetSummary()
    }
  }

  private def assertNetwork(): Unit = {
    assertEqual(
      findNetworkById(1),
      newNetworkDoc(
        1,
        active = false, // <--- !!!
        country = Some(Country.nl),
        newNetworkSummary(
          name = "network",
          routeType = RouteType.hiking,
        ),
        newNetworkDetail(
          lastUpdated = defaultTimestamp,
          relationLastUpdated = defaultTimestamp,
          tags = newNetworkTags("network")
        )
      )
    )
  }

  private def assertBaseNode(): Unit = {
    assertEqual(
      findBaseNodeById(1001),
      newBaseNodeDoc(
        1001,
        active = false,
        country = Some(Country.nl),
        version = 2, // <--
        latitude = "0",
        longitude = "0",
      )
    )
  }

  private def assertNode(): Unit = {
    assertEqual(
      findNodeById(1001),
      newNodeDoc(
        1001,
        labels = Seq.empty, // not active
        country = Some(Country.nl),
        version = 2, // <--
      )
    )
  }

  private def assertNetworkChange(): Unit = {
    assertEqual(
      findNetworkChangeById("123:1:1"),
      newNetworkChange(
        key = newChangeKey(elementId = 1),
        networkName = "network",
        changeType = ChangeType.Delete,
        country = Some(Country.nl),
        routeType = RouteType.hiking,
        nodes = IdDiffs(
          removed = Seq(
            1001
          )
        ),
        nodeDiffs = RefDiffs(
          removed = Seq(
            Ref(1001, "01")
          )
        ),
        investigate = true,
        impact = true,
      )
    )
  }

  private def assertNodeChange(): Unit = {
    assertEqual(
      findNodeChangeById("123:1:1001"),
      newNodeChange(
        key = newChangeKey(elementId = 1001),
        changeType = ChangeType.Delete,
        subsets = Seq(Subset.nlHiking),
        name = Some("01"),
        before = Some(
          newMetaData(version = 1)
        ),
        after = Some(
          newMetaData(version = 2)
        ),
        tagDiffs = Some(
          TagDiffs(
            mainTags = Seq(
              TagDetail(TagDetailType.Delete, "rwn_ref", Some("01"), None),
              TagDetail(TagDetailType.Delete, "network:type", Some("node_network"), None),
            )
          )
        ),
        removedFromNetwork = Seq(
          Ref(1, "network")
        ),
        facts = Seq(Fact.LostHikingNodeTag),
        investigate = true,
        impact = true,
        locationInvestigate = true,
        locationImpact = true
      )
    )
  }

  private def assertChangeSetSummary(): Unit = {
    assertEqual(
      findChangeSetSummaryById("123:1"),
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
        networkChanges = NetworkChanges(
          deletes = Seq(
            newChangeSetNetwork(
              Some(Country.nl),
              RouteType.hiking,
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
        subsetAnalyses = Seq(
          ChangeSetSubsetAnalysis(Subset.nlHiking, investigate = true)
        ),
        investigate = true
      )
    )
  }
}
