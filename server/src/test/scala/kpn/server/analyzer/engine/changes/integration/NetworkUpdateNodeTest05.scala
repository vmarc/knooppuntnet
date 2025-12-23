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
import kpn.core.test.TestObjects.newChange
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newChangeSetElementRef
import kpn.core.test.TestObjects.newChangeSetNetwork
import kpn.core.test.TestObjects.newChangeSetSummary
import kpn.core.test.TestObjects.newLocationChanges
import kpn.core.test.TestObjects.newMember
import kpn.core.test.TestObjects.newMetaData
import kpn.core.test.TestObjects.newNetworkChange
import kpn.core.test.TestObjects.newNodeChange
import kpn.core.test.TestObjects.newNodeDoc

class NetworkUpdateNodeTest05 extends IntegrationTest {

  test("network update - node that looses required tags and is removed from network becomes inactive") {

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
      .node(1002)
      .networkRelation(
        1,
        "name",
        Seq(
          newMember(MemberType.Node, 1001)
          // node 02 no longer part of the network
        )
      )

    testIntegration(dataBefore, dataAfter) {

      val node1001 = findNodeById(1001)

      process(
        newChange(
          ChangeAction.Modify,
          nodes = Seq(dataAfter.rawNodeWithId(1002)),
          relations = Seq(dataAfter.rawRelationWithId(1)),
        )
      )

      assert(watched.nodes.contains(1001))
      assert(!watched.nodes.contains(1002))
      assert(watched.networks.contains(1))

      findOrphanNodes() shouldBe empty
      database.routes shouldBe empty

      findNodeById(1001) should equal(node1001)
      assertNode1002()

      assertBaseNetwork()
      assertNetwork()
      assertNetworkChange()
      assertNoNodeChange(1001)
      assertNodeChange1002()
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

  private def assertNode1002(): Unit = {
    assertEqual(
      findNodeById(1002),
      newNodeDoc(
        1002,
        active = false,
        labels = Seq(
          Label.location("nl")
        ),
        country = Some(Country.nl),
        locations = Seq("nl"),
      )
    )
  }

  private def assertNetworkChange(): Unit = {
    assertEqual(
      findNetworkChangeById("1:1:1"),
      newNetworkChange(
        key = newChangeKey(elementId = 1),
        networkName = Some("name"),
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

  private def assertNodeChange1002(): Unit = {
    assertEqual(
      findNodeChangeById("1:1:1002"),
      newNodeChange(
        key = newChangeKey(elementId = 1002),
        changeType = ChangeType.Delete,
        subsets = Seq(Subset.nlHiking),
        locations = Seq("nl"),
        name = Some("02"),
        before = Some(
          newMetaData()
        ),
        after = None,
        removedFromNetwork = Seq(Ref(1, "name")),
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
          updates = Seq(
            newChangeSetNetwork(
              Some(Country.nl),
              RouteType.hiking,
              1,
              Some("name"),
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
              removed = Seq(
                newChangeSetElementRef(1002, "02", investigate = true),
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
