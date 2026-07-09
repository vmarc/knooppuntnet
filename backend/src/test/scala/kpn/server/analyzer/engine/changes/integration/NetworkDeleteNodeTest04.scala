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
import kpn.api.custom.Subset
import kpn.core.doc.Label
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newBaseNetworkDoc
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
import kpn.core.test.TestObjects.newRaw
import kpn.core.test.TestObjects.newRawNode
import kpn.core.test.TestObjects.newRawRelation
import kpn.core.test.Timestamps

class NetworkDeleteNodeTest04 extends IntegrationTest {

  test("network delete and node looses node tag") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01", version = 1) // before change
      .networkRelation( // before delete
        1,
        "network",
        Seq(
          newMember(MemberType.Node, 1001)
        )
      )

    val dataAfter = OverpassData()
      .node(1001, version = 2) // after change

    testIntegration(dataBefore, dataAfter) {
      process(
        Seq(
          newChange(ChangeAction.Modify, nodes = Seq(newRawNode(1001))),
          newChange(ChangeAction.Delete, relations = Seq(newRawRelation(1)))
        )
      )

      findOrphanNodes() shouldBe empty // the node does not become orphan, it is no longer a network node

      assert(!watched.networks.contains(1))
      assert(!watched.nodes.contains(1001))

      assertBaseNode()
      assertBaseNetwork()

      assertNode()
      assertNetwork()

      assertNodeChange()
      assertNetworkChange()
      assertChangeSetSummary()
    }
  }

  private def assertBaseNode(): Unit = {
    assertEqual(
      findBaseNodeById(1001),
      newBaseNodeDoc(
        1001,
        active = false,
        base = newNodeBaseData(
          raw = newRaw(
            version = 2 // <--
          ),
          country = Some(Country.nl),
          locations = Seq("nl"),
        )
      )
    )
  }

  private def assertBaseNetwork(): Unit = {
    assertEqual(
      findBaseNetworkById(1),
      newBaseNetworkDoc(
        1,
        active = false, // <--- !!!
        base = newNetworkBaseData(
          raw = newRaw(
            tags = newNetworkTags("network"),
          ),
          name = Some("network"),
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
        active = false,
        base = newNodeBaseData(
          raw = newRaw(
            version = 2 // <--
          ),
          country = Some(Country.nl),
          locations = Seq("nl")
        ),
        labels = Seq(
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
        changeType = ChangeType.Delete,
        subsets = Seq(Subset.nlHiking),
        locations = Seq("nl"),
        name = Some("01"),
        before = Some(
          newMetaData(version = 1)
        ),
        after = None,
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
      findChangeSetSummaryById("1:1"),
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
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
              removed = Seq(
                newChangeSetElementRef(1001, "01", investigate = true),
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
