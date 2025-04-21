package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetNetwork
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.NetworkChanges
import kpn.api.common.RouteType
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.common.Ref
import kpn.api.common.data.MemberType
import kpn.api.common.data.raw.RawMember
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.test.OverpassData

class NetworkDeleteTest01 extends IntegrationTest {

  test("network and network node delete") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkRelation(
        1,
        "network1",
        members = Seq(
          RawMember(MemberType.Node, 1001, None)
        )
      )

    val dataAfter = OverpassData.empty

    testIntegration(dataBefore, dataAfter) {

      watched.networks.ids should contain(1)
      watched.nodes.ids should contain(1001)

      process(
        ChangeAction.Delete,
        newRawNode(1001),
        newRawRelation(1)
      )
      watched.networks.ids shouldNot contain(1)
      watched.nodes.ids shouldNot contain(1001)

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
      findBaseNodeById(1001L),
      newBaseNodeDoc(
        1001L,
        active = false,
        country = Some(Country.nl),
      )
    )
  }

  private def assertBaseNetwork(): Unit = {
    assertEqual(
      findBaseNetworkById(1),
      newBaseNetworkDoc(
        1L,
        active = false,
        name = Some("network1"),
        changeSetId = 1,
        tags = Tags.from(
          "network:type" -> "node_network",
          "type" -> "network",
          "network" -> "rwn",
          "name" -> "network1",
        )
      )
    )
  }

  private def assertNetwork(): Unit = {
    assertEqual(
      findNetworkById(1),
      newNetworkDoc(
        1L,
        active = false,
        country = Some(Country.nl),
        summary = newNetworkSummary(
          name = "network1",
        ),
        detail = newNetworkDetail(
          tags = Tags.from(
            "network:type" -> "node_network",
            "type" -> "network",
            "network" -> "rwn",
            "name" -> "network1",
          )
        )
      )
    )
  }

  private def assertNode(): Unit = {
    assertEqual(
      findNodeById(1001L),
      newNodeDoc(
        1001L,
        labels = Seq.empty,
        country = Some(Country.nl),
      )
    )
  }

  private def assertChangeSetSummary(): Unit = {
    assertEqual(
      findChangeSetSummaryById("123:1"),
      newChangeSetSummary(
        key = ChangeKey(1, Timestamp(2015, 8, 11, 0, 0, 0), 123, 0),
        subsets = Seq(Subset.nlHiking),
        timestampFrom = Timestamp(2015, 8, 11, 0, 0, 2),
        timestampUntil = Timestamp(2015, 8, 11, 0, 0, 3),
        networkChanges = NetworkChanges(
          deletes = Seq(
            ChangeSetNetwork(
              country = Some(Country.nl),
              routeType = RouteType.hiking,
              networkId = 1,
              networkName = "network1",
              routeChanges = ChangeSetElementRefs(),
              nodeChanges = ChangeSetElementRefs(
                removed = Seq(
                  newChangeSetElementRef(1001, "01", investigate = true)
                )
              ),
              happy = false,
              investigate = true
            )
          )
        ),
        subsetAnalyses = Seq(
          ChangeSetSubsetAnalysis(
            Subset.nlHiking,
            investigate = true
          )
        ),
        investigate = true
      )
    )
  }

  private def assertNetworkChange(): Unit = {
    assertEqual(
      findNetworkChangeById("123:1:1"),
      newNetworkChange(
        key = newChangeKey(elementId = 1),
        networkName = "network1",
        changeType = ChangeType.Delete,
        country = Some(Country.nl),
        routeType = RouteType.hiking,
        nodes = IdDiffs(removed = Seq(1001)),
        nodeDiffs = RefDiffs(
          removed = Seq(
            Ref(1001, "01"),
          )
        ),
        investigate = true,
        impact = true,
      )
    )
  }

  private def assertNodeChange(): Unit = {
    val nodeChange = findNodeChangeById("123:1:1001")
    assertEqual(
      nodeChange,
      newNodeChange(
        key = newChangeKey(elementId = 1001),
        changeType = ChangeType.Delete,
        subsets = Seq(Subset.nlHiking),
        name = Some("01"),
        before = Some(newMetaData()),
        removedFromNetwork = Seq(Ref(1, "network1")),
        facts = Seq(Fact.Deleted),
        investigate = true,
        impact = true,
        locationInvestigate = true,
        locationImpact = true
      )
    )
  }
}
