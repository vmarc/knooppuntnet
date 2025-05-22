package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.NetworkChanges
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.changes.ChangeAction
import kpn.api.common.common.Ref
import kpn.api.common.data.MemberType
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.api.common.diff.TagDiff
import kpn.api.common.diff.TagDiffs
import kpn.api.custom.Change
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.doc.Label
import kpn.core.test.OverpassData
import kpn.core.test.Timestamps

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
          newMember(MemberType.Node, 1001)
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

      process(
        Seq(
          Change(ChangeAction.Modify, Seq(newRawNode(1001))),
          Change(ChangeAction.Delete, Seq(newRawRelation(1)))
        )
      )

      watched.networks.ids shouldNot contain(1)
      watched.nodes.ids should contain(1001)

      assertBaseNode()
      assertBaseNetwork()
      assertNode()
      assertNetwork()
      assertNetworkChange()
      assertNodeChange()
      assertChangeSetSummary()
    }
  }

  private def assertBaseNode(): Unit = {
    assertEqual(
      findBaseNodeById(1001),
      newBaseNodeDoc(
        1001,
        name = Some("02"),
        names = Seq(
          newNodeName(RouteType.cycling, RouteScope.regional, "02")
        ),
        version = 2,
        tags = Tags.from(
          "network:type" -> "node_network",
          "rcn_ref" -> "02"
        ),
        country = Some(Country.nl),
        locations = Seq("nl"),
        tiles = Seq(
          "cycling-9-256-256",
          "cycling-10-512-512",
          "cycling-11-1024-1024",
          "cycling-12-2048-2048",
          "cycling-13-4096-4096",
          "cycling-14-8192-8192"
        )
      )
    )
  }

  private def assertBaseNetwork(): Unit = {
    val networkDoc = findBaseNetworkById(1)
    networkDoc._id should equal(1)
  }

  private def assertNode(): Unit = {
    assertEqual(
      findNodeById(1001),
      newNodeDoc(
        1001,
        labels = Seq(
          Label.routeType(RouteType.cycling),
          Label.location("nl")
        ),
        country = Some(Country.nl),
        name = Some("02"),
        names = Seq(
          newNodeName(RouteType.cycling, RouteScope.regional, "02")
        ),
        version = 2, // <--
        tags = Tags.from(
          "network:type" -> "node_network",
          "rcn_ref" -> "02"
        ),
        locations = Seq("nl"),
      )
    )
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
          lastUpdated = Timestamps.default,
          relationLastUpdated = Timestamps.default,
          tags = newNetworkTags("network")
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
        changeType = ChangeType.Update,
        subsets = Seq(
          Subset.nlHiking,
          Subset.nlBicycle
        ),
        locations = Seq("nl"),
        name = Some("02"),
        before = Some(
          newMetaData(version = 1)
        ),
        after = Some(
          newMetaData(version = 2)
        ),
        tagDiffs = Some(
          TagDiffs(
            mainTags = Seq(
              TagDiff.delete("rwn_ref", "01"),
              TagDiff.same("rcn_ref", "02"),
              TagDiff.same("network:type", "node_network")
            )
          )
        ),
        removedFromNetwork = Seq(
          Ref(1, "network")
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
        locationChanges = Seq(
          newLocationChanges(
            routeType = RouteType.hiking,
            locationNames = Seq("nl"),
            nodeChanges = ChangeSetElementRefs(
              updated = Seq(
                newChangeSetElementRef(1001, "02", investigate = true),
              )
            ),
            investigate = true
          ),
          newLocationChanges(
            routeType = RouteType.cycling,
            locationNames = Seq("nl"),
            nodeChanges = ChangeSetElementRefs(
              updated = Seq(
                newChangeSetElementRef(1001, "02", investigate = true),
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
