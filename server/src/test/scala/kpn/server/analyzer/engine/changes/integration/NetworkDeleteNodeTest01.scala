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
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.test.OverpassData

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

      assert(!watched.networks.contains(1))
      assert(watched.nodes.contains(1001))

      assert(database.routeChanges.isEmpty)

      assertBaseNetwork()
      assertNetwork()
      assertNetworkChange()
      assertNodeChange()
      assertChangeSetSummary()

      assertOrphanNode()
    }
  }

  private def assertBaseNetwork(): Unit = {
    assertEqual(
      findBaseNetworkById(1),
      newNetwork(
        1,
        active = false,
        tags = Tags.from(
          "network:type" -> "node_network",
          "type" -> "network",
          "network" -> "rwn",
          "name" -> "network-name"
        )
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
        investigate = true
      )
    )
  }

  private def assertOrphanNode(): Unit = {
    assertEqual(
      findOrphanNodeById("nl:hiking:1001"),
      newOrphanNodeDoc(
        country = Country.nl,
        routeType = RouteType.hiking,
        nodeId = 1001,
        name = "01"
      )
    )
  }
}
