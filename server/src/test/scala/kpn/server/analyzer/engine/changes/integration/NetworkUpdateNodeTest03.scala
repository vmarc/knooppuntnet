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
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newChangeSetElementRef
import kpn.core.test.TestObjects.newChangeSetNetwork
import kpn.core.test.TestObjects.newChangeSetSummary
import kpn.core.test.TestObjects.newLocationChanges
import kpn.core.test.TestObjects.newMember
import kpn.core.test.TestObjects.newMetaData
import kpn.core.test.TestObjects.newNetworkChange
import kpn.core.test.TestObjects.newNodeChange

class NetworkUpdateNodeTest03 extends IntegrationTest {

  test("network update - node that is no longer part of the network after update, does not become orphan node if still referenced in another network") {

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
      .networkRelation(
        2,
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
      .networkRelation(
        2,
        "name",
        Seq(
          newMember(MemberType.Node, 1001),
          newMember(MemberType.Node, 1002) // node 02 still referenced in other network
        )
      )

    testIntegration(dataBefore, dataAfter) {

      val network2 = findBaseNetworkById(2)
      val networkInfo2 = findNetworkById(2)

      process(ChangeAction.Modify, dataAfter.rawRelationWithId(1))

      watched.nodes.ids should contain(1001)
      watched.nodes.ids should contain(1002)
      watched.networks.ids should contain(1)
      watched.networks.ids should contain(2)

      findOrphanNodes() shouldBe empty

      // network 2 has not changed
      assertEqual(findBaseNetworkById(2), network2)
      assertEqual(findNetworkById(2), networkInfo2)

      assertBaseNetwork()
      assertNetwork()
      assertNetworkChange()
      assertNodeChange1002()
      assertChangeSetSummary()
    }
  }

  private def assertBaseNetwork(): Unit = {
    val networkDoc = findBaseNetworkById(1)
    networkDoc._id should equal(1)
  }

  private def assertNetwork(): Unit = {
    val networkInfoDoc = findNetworkById(1)
    networkInfoDoc._id should equal(1)
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
          removed = Seq(
            Ref(1002, "02")
          )
        ),
        investigate = true,
        impact = true,
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
                newChangeSetElementRef(1002, "02")
              )
            ),
          )
        ),
        investigate = true
      )
    )
  }
}
