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
import kpn.api.common.common.Ref
import kpn.api.common.data.MemberType
import kpn.api.common.data.raw.RawMember
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.doc.Label
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newBaseNetworkDoc
import kpn.core.test.TestObjects.newBaseNodeDoc
import kpn.core.test.TestObjects.newChange
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newChangeSetElementRef
import kpn.core.test.TestObjects.newChangeSetSummary
import kpn.core.test.TestObjects.newLocationChanges
import kpn.core.test.TestObjects.newMetaData
import kpn.core.test.TestObjects.newNetworkBaseData
import kpn.core.test.TestObjects.newNetworkChange
import kpn.core.test.TestObjects.newNetworkDetail
import kpn.core.test.TestObjects.newNetworkDoc
import kpn.core.test.TestObjects.newNodeBaseData
import kpn.core.test.TestObjects.newNodeChange
import kpn.core.test.TestObjects.newNodeDoc
import kpn.core.test.TestObjects.newRaw
import kpn.core.test.TestObjects.newRawNode
import kpn.core.test.TestObjects.newRawRelation

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

      assert(watched.networks.contains(1))
      assert(watched.nodes.contains(1001))

      process(
        newChange(
          ChangeAction.Delete,
          nodes = Seq(
            newRawNode(1001)
          ),
          relations = Seq(newRawRelation(1)
          )
        )
      )
      assert(!watched.networks.contains(1))
      assert(!watched.nodes.contains(1001))

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
        base = newNodeBaseData(
          country = Some(Country.nl),
          locations = Seq("nl")
        )
      )
    )
  }

  private def assertBaseNetwork(): Unit = {
    assertEqual(
      findBaseNetworkById(1),
      newBaseNetworkDoc(
        1L,
        active = false,
        base = newNetworkBaseData(
          raw = newRaw(
            tags = Tags.from(
              "network:type" -> "node_network",
              "type" -> "network",
              "network" -> "rwn",
              "name" -> "network1"
            )
          ),
          name = Some("network1"),
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
        1L,
        active = false,
        country = Some(Country.nl),
        base = newNetworkBaseData(
          raw = newRaw(
            tags = Tags.from(
              "network:type" -> "node_network",
              "type" -> "network",
              "network" -> "rwn",
              "name" -> "network1"
            )
          ),
          name = Some("network1"),
        ),
        detail = newNetworkDetail(
        )
      )
    )
  }

  private def assertNode(): Unit = {
    assertEqual(
      findNodeById(1001L),
      newNodeDoc(
        1001L,
        active = false,
        base = newNodeBaseData(
          country = Some(Country.nl),
          locations = Seq("nl")
        ),
        labels = Seq(
          Label.location("nl")
        )
      )
    )
  }

  private def assertChangeSetSummary(): Unit = {
    assertEqual(
      findChangeSetSummaryById("1:1"),
      newChangeSetSummary(
        key = newChangeKey(),
        subsets = Seq(Subset.nlHiking),
        locations = Seq("nl"),
        timestampFrom = Timestamp(2015, 8, 11, 0, 0, 2),
        timestampUntil = Timestamp(2015, 8, 11, 0, 0, 3),
        networkChanges = NetworkChanges(
          deletes = Seq(
            ChangeSetNetwork(
              country = Some(Country.nl),
              routeType = RouteType.hiking,
              networkId = 1,
              networkName = Some("network1"),
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
        locationChanges = Seq(
          newLocationChanges(
            routeType = RouteType.hiking,
            locationNames = Seq("nl"),
            nodeChanges = ChangeSetElementRefs(
              removed = Seq(
                newChangeSetElementRef(1001, "01", investigate = true),
              )
            ),
            investigate = true
          ),
        ),
        investigate = true
      )
    )
  }

  private def assertNetworkChange(): Unit = {
    assertEqual(
      findNetworkChangeById("1:1:1"),
      newNetworkChange(
        key = newChangeKey(elementId = 1),
        networkName = Some("network1"),
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
    val nodeChange = findNodeChangeById("1:1:1001")
    assertEqual(
      nodeChange,
      newNodeChange(
        key = newChangeKey(elementId = 1001),
        changeType = ChangeType.Delete,
        subsets = Seq(Subset.nlHiking),
        locations = Seq("nl"),
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
