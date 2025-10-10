package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.NetworkChanges
import kpn.api.common.RouteType
import kpn.api.common.changes.ChangeAction
import kpn.api.common.common.Ref
import kpn.api.common.data.MemberType
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.doc.Label
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newBaseNetworkDoc
import kpn.core.test.TestObjects.newBaseNodeDoc
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
import kpn.core.test.TestObjects.newNetworkSummary
import kpn.core.test.TestObjects.newNodeChange
import kpn.core.test.TestObjects.newNodeDoc
import kpn.core.test.TestObjects.newNodeName
import kpn.core.test.TestObjects.newOrphanNodeInfo
import kpn.core.test.TestObjects.newRawRelation

class NetworkDeleteNodeTest01 extends IntegrationTest {

  test("network delete - node becomes orphan") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkRelation( // delete
        1,
        "network-name",
        Seq(
          newMember(MemberType.Node, 1001)
        )
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Delete, newRawRelation(1))

      watched.networks shouldNot contain(1)
      watched.nodes should contain(1001)

      database.routeChanges shouldBe empty

      assertBaseNode()
      assertBaseNetwork()

      assertNode()
      assertNetwork()

      assertNodeChange()
      assertNetworkChange()
      assertChangeSetSummary()

      assertOrphanNode()
    }
  }

  private def assertBaseNode(): Unit = {
    assertEqual(
      findBaseNodeById(1001),
      newBaseNodeDoc(
        1001,
        name = Some("01"),
        names = Seq(
          newNodeName(name = "01")
        ),
        tags = Tags.from(
          "rwn_ref" -> "01",
          "network:type" -> "node_network"
        ),
        country = Some(Country.nl),
        locations = Seq("nl"),
        tiles = Seq(
          "hiking-12-2047-2047",
          "hiking-12-2047-2048",
          "hiking-12-2048-2047",
          "hiking-12-2048-2048",
          "hiking-13-4095-4095",
          "hiking-13-4095-4096",
          "hiking-13-4096-4095",
          "hiking-13-4096-4096"
        )
      )
    )
  }

  private def assertBaseNetwork(): Unit = {
    assertEqual(
      findBaseNetworkById(1),
      newBaseNetworkDoc(
        1,
        active = false,
        name = Some("network-name"),
        changeSetId = 1,
        tags = Tags.from(
          "network:type" -> "node_network",
          "type" -> "network",
          "network" -> "rwn",
          "name" -> "network-name"
        ),
        nodeIds = Seq(
          1001
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
          Label.routeType(RouteType.hiking),
          Label.location("nl")
        ),
        country = Some(Country.nl),
        locations = Seq("nl"),
        name = Some("01"),
        names = Seq(
          newNodeName(name = "01")
        ),
        tags = Tags.from(
          "rwn_ref" -> "01",
          "network:type" -> "node_network"
        ),
      )
    )
  }

  private def assertNetwork(): Unit = {
    assertEqual(
      findNetworkById(1),
      newNetworkDoc(
        1,
        active = false,
        country = Some(Country.nl),
        summary = newNetworkSummary(
          name = "network-name",
        ),
        detail = newNetworkDetail(
          tags = Tags.from(
            "network:type" -> "node_network",
            "type" -> "network",
            "network" -> "rwn",
            "name" -> "network-name"
          )
        )
      )
    )
  }

  private def assertNetworkChange(): Unit = {
    assertEqual(
      findNetworkChangeById("123:1:1"),
      newNetworkChange(
        key = newChangeKey(elementId = 1),
        networkName = "network-name",
        changeType = ChangeType.Delete,
        country = Some(Country.nl),
        routeType = RouteType.hiking,
        nodes = IdDiffs(
          removed = Seq(1001)
        ),
        nodeDiffs = RefDiffs(
          removed = Seq(Ref(1001, "01"))
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
        subsets = Seq(Subset.nlHiking),
        locations = Seq("nl"),
        name = Some("01"),
        before = Some(
          newMetaData()
        ),
        after = Some(
          newMetaData()
        ),
        removedFromNetwork = Seq(
          Ref(1, "network-name")
        ),
        investigate = true,
        impact = true
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
          deletes = Seq(
            newChangeSetNetwork(
              Some(Country.nl),
              RouteType.hiking,
              1,
              "network-name",
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
                newChangeSetElementRef(1001, "01"),
              ),
            )
          )
        ),
        investigate = true
      )
    )
  }

  private def assertOrphanNode(): Unit = {
    assertEqual(
      findOrphanNode(Subset.nlHiking, 1001),
      newOrphanNodeInfo(
        nodeId = 1001,
        name = "01"
      )
    )
  }
}
