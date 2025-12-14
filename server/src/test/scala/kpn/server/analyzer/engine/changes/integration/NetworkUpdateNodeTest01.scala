package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.LatLonImpl
import kpn.api.common.NetworkChanges
import kpn.api.common.RouteType
import kpn.api.common.changes.ChangeAction
import kpn.api.common.common.Ref
import kpn.api.common.data.MemberType
import kpn.api.common.data.raw.RawMember
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newBaseNetworkDoc
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newChangeSetElementRef
import kpn.core.test.TestObjects.newChangeSetNetwork
import kpn.core.test.TestObjects.newChangeSetSummary
import kpn.core.test.TestObjects.newLocationChanges
import kpn.core.test.TestObjects.newMember
import kpn.core.test.TestObjects.newMetaData
import kpn.core.test.TestObjects.newNetworkChange
import kpn.core.test.TestObjects.newNetworkDetail
import kpn.core.test.TestObjects.newNetworkDoc
import kpn.core.test.TestObjects.newNetworkInfoNodeDetail
import kpn.core.test.TestObjects.newNetworkSummary
import kpn.core.test.TestObjects.newNodeChange
import kpn.core.test.TestObjects.newOrphanNodeInfo

class NetworkUpdateNodeTest01 extends IntegrationTest {

  test("network update - node that is no longer part of the network after update, becomes orphan node if also not referenced in any other network or orphan route") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
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
      .networkNode(1002, "02")
      .networkRelation(
        1,
        "name",
        Seq(
          newMember(MemberType.Node, 1001)
          // node 02 no longer part of the network
        )
      )

    testIntegration(dataBefore, dataAfter) {

      val baseNode1001 = findBaseNodeById(1001)
      val baseNode1002 = findBaseNodeById(1002)
      val node1001 = findNodeById(1001)
      val node1002 = findNodeById(1002)

      processRelation(ChangeAction.Modify, dataAfter.rawRelationWithId(1))

      assert(watched.nodes.contains(1001))
      assert(watched.nodes.contains(1002))
      assert(watched.networks.contains(1))

      database.routes shouldBe empty

      assertEqual(findBaseNodeById(1001), baseNode1001) // no change
      assertEqual(findBaseNodeById(1002), baseNode1002) // no change
      assertEqual(findNodeById(1001), node1001) // no change
      assertEqual(
        findNodeById(1002),
        node1002.copy( // no longer part of the network
          networkRelationReferences = Seq.empty
        )
      )

      assertEqual(
        findOrphanNode(Subset.nlHiking, 1002),
        newOrphanNodeInfo(
          1002,
          "02"
        )
      )

      assertBaseNetwork()
      assertNetwork()
      assertNoNodeChange(1001)
      assertNodeChange1002()
      assertNetworkChange()
      assertChangeSetSummary()
    }
  }

  private def assertBaseNetwork(): Unit = {
    assertEqual(
      findBaseNetworkById(1),
      newBaseNetworkDoc(
        1,
        name = Some("name"),
        changeSetId = 1,
        members = Seq(
          RawMember(MemberType.Node, 1001, None)
        ),
        tags = Tags.from(
          "network:type" -> "node_network",
          "type" -> "network",
          "network" -> "rwn",
          "name" -> "name"
        ),
        nodeIds = Seq(
          1001
        )
      )
    )
  }

  private def assertNetwork(): Unit = {
    assertEqual(
      findNetworkById(1),
      newNetworkDoc(
        1,
        country = Some(Country.nl),
        summary = newNetworkSummary(
          name = "name",
          nodeCount = 1,
        ),
        detail = newNetworkDetail(
          tags = Tags.from(
            "network:type" -> "node_network",
            "type" -> "network",
            "network" -> "rwn",
            "name" -> "name"
          ),
          center = Some(LatLonImpl("0.0", "0.0")),
        ),
        nodes = Seq(
          newNetworkInfoNodeDetail(
            1001,
            name = "01",
            definedInRelation = true
          )
        ),
        members = Seq(
          RawMember(MemberType.Node, 1001, None)
        )
      )
    )
  }

  private def assertNodeChange1002(): Unit = {
    assertEqual(
      findNodeChangeById("123:1:1002"),
      newNodeChange(
        key = newChangeKey(elementId = 1002),
        changeType = ChangeType.Update,
        subsets = Seq(Subset.nlHiking),
        locations = Seq("nl"),
        name = Some("02"),
        before = Some(
          newMetaData()
        ),
        after = Some(
          newMetaData()
        ),
        removedFromNetwork = Seq(Ref(1, "name")),
        investigate = true,
        impact = true
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
        nodeDiffs = RefDiffs(removed = Seq(Ref(1002, "02"))),
        investigate = true,
        impact = true,
      )
    )
  }

  private def assertChangeSetSummary(): Unit = {
    assertEqual(
      findChangeSetSummaryById("123:1"),
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
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
                newChangeSetElementRef(1002, "02"),
              )
            )
          )
        ),
        investigate = true
      )
    )
  }
}
