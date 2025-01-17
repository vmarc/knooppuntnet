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
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.doc.Label
import kpn.core.test.OverpassData

class NetworkDeleteNodeTest04 extends IntegrationTest {

  test("network delete - node looses node tag") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01", version = 1)
      .networkRelation(
        1,
        "network",
        Seq(
          newMember(MemberType.Node, 1001)
        )
      )

    val dataAfter = OverpassData()
      .node(1001, version = 2)

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Delete, newRawRelation(1))

      assert(database.orphanNodes.isEmpty) // the node does not become orphan, it is no longer a network node

      assert(!watched.networks.contains(1))
      assert(!watched.nodes.contains(1001))

      assertNetwork()
      assertNode()
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

  private def assertNode(): Unit = {
    assertEqual(
      findNodeById(1001),
      newNodeDoc(
        1001,
        labels = Seq(
          Label.routeType(RouteType.hiking)
          // not active
        ),
        country = Some(Country.nl),
        name = Some("01"),
        names = Seq(newNodeName(name = "01")),
        version = 1, // <--
        tags = Tags.from(
          "rwn_ref" -> "01",
          "network:type" -> "node_network",
        )
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
        //  networkDataUpdate = None,
        //  nodes= IdDiffs.empty,
        //  ways = IdDiffs.empty,
        //  relations = IdDiffs.empty,
        nodeDiffs = RefDiffs(
          removed = Seq(
            Ref(1001, "01")
          )
        ),
        //  routeDiffs = RefDiffs.empty,
        //  extraNodeDiffs = IdDiffs.empty,
        //  extraWayDiffs = IdDiffs.empty,
        //  extraRelationDiffs = IdDiffs.empty,
        //  happy = false,
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
        after = None,
        tagDiffs = None,
        removedFromNetwork = Seq(
          Ref(1, "network")
        ),
        facts = Seq(Fact.Deleted),
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
