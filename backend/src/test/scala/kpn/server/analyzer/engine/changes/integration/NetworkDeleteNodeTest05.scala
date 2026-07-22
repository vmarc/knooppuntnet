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
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.time.Timestamps
import kpn.core.doc.Label
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newBaseNodeDoc
import kpn.core.test.TestObjects.newChange
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newChangeSetElementRef
import kpn.core.test.TestObjects.newChangeSetNetwork
import kpn.core.test.TestObjects.newChangeSetSummary
import kpn.core.test.TestObjects.newLocationChanges
import kpn.core.test.TestObjects.newMember
import kpn.core.test.TestObjects.newMetaData
import kpn.core.test.TestObjects.newNetworkBaseData
import kpn.core.test.TestObjects.newNetworkChange
import kpn.core.test.TestObjects.newNetworkDetail
import kpn.core.test.TestObjects.newNetworkDoc
import kpn.core.test.TestObjects.newNetworkTags
import kpn.core.test.TestObjects.newNodeBaseData
import kpn.core.test.TestObjects.newNodeChange
import kpn.core.test.TestObjects.newNodeDoc
import kpn.core.test.TestObjects.newNodeName
import kpn.core.test.TestObjects.newRaw
import kpn.core.test.TestObjects.newRawNode
import kpn.core.test.TestObjects.newRawRelation

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
          newChange(ChangeAction.Modify, nodes = Seq(newRawNode(1001))),
          newChange(ChangeAction.Delete, relations = Seq(newRawRelation(1)))
        )
      )

      assert(!watched.networks.contains(1))
      assert(watched.nodes.contains(1001))

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
        base = newNodeBaseData(
          raw = newRaw(
            version = 2,
            tags = Tags.from(
              "network:type" -> "node_network",
              "rcn_ref" -> "02"
            )
          ),
          name = Some("02"),
          names = Seq(
            newNodeName(RouteType.cycling, RouteScope.regional, "02")
          ),
          country = Some(Country.nl),
          locations = Seq("nl")
        ),
        tiles = Seq(
          "cycling-12-2047-2047",
          "cycling-12-2047-2048",
          "cycling-12-2048-2047",
          "cycling-12-2048-2048",
          "cycling-13-4095-4095",
          "cycling-13-4095-4096",
          "cycling-13-4096-4095",
          "cycling-13-4096-4096"
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
        base = newNodeBaseData(
          raw = newRaw(
            version = 2, // <--
            tags = Tags.from(
              "network:type" -> "node_network",
              "rcn_ref" -> "02"
            ),
          ),
          name = Some("02"),
          names = Seq(
            newNodeName(RouteType.cycling, RouteScope.regional, "02")
          ),
          country = Some(Country.nl),
          locations = Seq("nl"),
        ),
        labels = Seq(
          Label.routeType(RouteType.cycling),
          Label.location("nl")
        )
      )
    )
  }

  private def assertNetwork(): Unit = {
    assertEqual(
      findNetworkById(1),
      newNetworkDoc(
        1,
        active = false, // <--- !!!
        base = newNetworkBaseData(
          raw = newRaw(
            tags = newNetworkTags("network")
          ),
          name = Some("network"),
          routeType = RouteType.hiking,
        ),
        country = Some(Country.nl),
        newNetworkDetail(
          lastUpdated = Timestamps.default,
          relationLastUpdated = Timestamps.default,
        )
      )
    )
  }

  private def assertNetworkChange(): Unit = {
    assertEqual(
      findNetworkChangeById("1:1:1"),
      newNetworkChange(
        key = newChangeKey(elementId = 1),
        networkName = Some("network"),
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
      findNodeChangeById("1:1:1001"),
      newNodeChange(
        key = newChangeKey(elementId = 1001),
        changeType = ChangeType.Update,
        subsets = Seq(
          Subset.nlHiking,
          Subset.nlCycling
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
      findChangeSetSummaryById("1:1"),
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
              Some("network"),
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
