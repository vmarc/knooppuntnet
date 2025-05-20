package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.NetworkChanges
import kpn.api.common.NodeName
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.changes.ChangeAction
import kpn.api.common.common.Ref
import kpn.api.common.data.MemberType
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.api.common.diff.TagDiff
import kpn.api.common.diff.TagDiffs
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.doc.Label
import kpn.core.test.OverpassData

class NetworkUpdateNodeTest06 extends IntegrationTest {

  test("network update - removed node that looses required tags, but still has tags of other routeType does not become inactive") {

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
          newMember(MemberType.Node, 1001),
          newMember(MemberType.Node, 1002)
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
          newMember(MemberType.Node, 1001)
          // node 02 no longer part of the network
        )
      )

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Modify, dataAfter.rawNodeWithId(1002), dataAfter.rawRelationWithId(1))

      watched.nodes.ids should contain(1001)
      watched.nodes.ids should contain(1002)
      watched.networks.ids should contain(1)

      assertBaseNetwork()
      assertNetwork()

      assertNode()
      assertOrphanNode()
      assertNetworkChange()
      assertNodeChange()
      assertChangeSetSummary()
    }
  }

  private def assertBaseNetwork(): Unit = {
    val baseNetworkDoc = findBaseNetworkById(1)
    baseNetworkDoc._id should equal(1)
  }

  private def assertNetwork(): Unit = {
    val networkDoc = findNetworkById(1)
    networkDoc._id should equal(1)
  }

  private def assertNode(): Unit = {
    assertEqual(
      findNodeById(1002),
      newNodeDoc(
        1002,
        labels = Seq(
          Label.active,
          Label.routeType(RouteType.cycling),
          Label.location("nl")
        ),
        country = Some(Country.nl),
        name = Some("03"),
        names = Seq(
          NodeName(
            RouteType.cycling,
            RouteScope.regional,
            "03",
            None,
            proposed = false
          )
        ),
        tags = Tags.from(
          "network:type" -> "node_network",
          "rcn_ref" -> "03",
        ),
        locations = Seq("nl"),
      )
    )
  }

  private def assertOrphanNode(): Unit = {
    assertEqual(
      findOrphanNodeById("nl:cycling:1002"),
      newOrphanNodeDoc(
        country = Country.nl,
        routeType = RouteType.cycling,
        nodeId = 1002,
        name = "03"
      )
    )
  }

  private def assertNetworkChange(): Unit = {
    assertEqual(
      findNetworkChangeById("123:1:1"),
      newNetworkChange(
        key = newChangeKey(elementId = 1),
        networkName = "name",
        changeType = ChangeType.Update,
        country = Some(Country.nl),
        routeType = RouteType.hiking,
        nodes = IdDiffs(
          removed = Seq(1002)
        ),
        nodeDiffs = RefDiffs(
          removed = Seq(Ref(1002, "02"))
        ),
        investigate = true,
        impact = true,
      )
    )
  }

  private def assertNodeChange(): Unit = {
    assertEqual(
      findNodeChangeById("123:1:1002"),
      newNodeChange(
        key = newChangeKey(elementId = 1002),
        changeType = ChangeType.Update,
        subsets = Seq(
          Subset.nlHiking,
          Subset.nlBicycle
        ),
        locations = Seq("nl"),
        name = Some("03"),
        before = Some(
          newMetaData()
        ),
        after = Some(
          newMetaData()
        ),
        tagDiffs = Some(
          TagDiffs(
            Seq(
              TagDiff.delete("rwn_ref", "02"),
              TagDiff.same("rcn_ref", "03"),
              TagDiff.same("network:type", "node_network")
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
    assertEqual(
      findChangeSetSummaryById("123:1"),
      newChangeSetSummary(
        subsets = Seq(
          Subset.nlHiking
        ),
        locations = Seq("nl"),
        networkChanges = NetworkChanges(
          updates = Seq(
            newChangeSetNetwork(
              Some(Country.nl),
              RouteType.hiking,
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
        locationChanges = Seq(
          newLocationChanges(
            routeType = RouteType.hiking,
            locationNames = Seq("nl"),
            nodeChanges = ChangeSetElementRefs(
              updated = Seq(
                newChangeSetElementRef(1002, "03", investigate = true),
              )
            ),
            investigate = true
          ),
          newLocationChanges(
            routeType = RouteType.cycling,
            locationNames = Seq("nl"),
            nodeChanges = ChangeSetElementRefs(
              updated = Seq(
                newChangeSetElementRef(1002, "03", investigate = true),
              )
            ),
            investigate = true
          )
        ),
        investigate = true
      )
    )
  }
}
