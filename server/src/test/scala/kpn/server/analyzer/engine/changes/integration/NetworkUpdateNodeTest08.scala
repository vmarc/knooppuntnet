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
import kpn.core.test.TestObjects.newOrphanNodeDoc

class NetworkUpdateNodeTest08 extends IntegrationTest {

  test("network update - an orphan node that is added to the network is no longer orphan") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02") // orphan node, not referenced by the network
      .networkRelation(
        1,
        "name",
        Seq(
          newMember(MemberType.Node, 1001)
          // the network does not reference the orphan node
        )
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .networkRelation(
        1,
        "name",
        Seq(
          newMember(MemberType.Node, 1001),
          newMember(MemberType.Node, 1002) // reference to the previous orphan node
        )
      )

    testIntegration(dataBefore, dataAfter) {

      assertEqual(
        findOrphanNodeById("nl:hiking:1002"),
        newOrphanNodeDoc(
          country = Country.nl,
          routeType = RouteType.hiking,
          nodeId = 1002,
          name = "02"
        )
      )

      process(ChangeAction.Modify, dataAfter.rawRelationWithId(1))

      database.orphanNodes.findAll() shouldBe empty

      watched.nodes.ids should contain(1001)
      watched.nodes.ids should contain(1002)

      assertNetwork()
      assertNetworkChange()
      assertNodeChange1002()
      assertChangeSetSummary()
    }
  }

  private def assertNetwork(): Unit = {
    val networkDoc = findBaseNetworkById(1)
    networkDoc._id should equal(1)
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
          added = Seq(1002)
        ),
        nodeDiffs = RefDiffs(
          added = Seq(Ref(1002, "02"))
        ),
        happy = true,
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
        addedToNetwork = Seq(Ref(1, "name")),
        happy = true,
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
                added = Seq(newChangeSetElementRef(1002, "02", happy = true))
              ),
              happy = true
            )
          )
        ),
        subsetAnalyses = Seq(
          ChangeSetSubsetAnalysis(Subset.nlHiking, happy = true)
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
        happy = true
      )
    )
  }
}
