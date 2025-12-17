package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.ElementChangeType
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
import kpn.core.test.TestObjects.newRaw
import kpn.core.test.TestObjects.newRawNode
import kpn.core.test.TestObjects.newRawRelation
import kpn.core.test.TestObjects.newRawWay
import kpn.core.test.TestObjects.newRouteChange
import kpn.core.test.TestObjects.newRouteData
import kpn.core.test.TestObjects.newRouteNode
import kpn.core.test.TestObjects.newRouteNodeChange
import kpn.core.test.TestObjects.newRouteTags

class NetworkUpdateRouteTest03 extends IntegrationTest {

  test("network update - route no longer part of the network after deletion") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .networkNode(1003, "03")
      .way(101, 1001, 1002)
      .way(102, 1002, 1003)
      .route(11, "01-02", Seq(newMember(MemberType.Way, 101)))
      .route(12, "02-03", Seq(newMember(MemberType.Way, 102)))
      .networkRelation(
        1,
        "name",
        Seq(
          newMember(MemberType.Relation, 11),
          newMember(MemberType.Relation, 12)
        )
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(11, "01-02", Seq(newMember(MemberType.Way, 101)))
      .networkRelation(
        1,
        "name",
        Seq(
          newMember(MemberType.Relation, 11)
        )
      )

    testIntegration(dataBefore, dataAfter) {

      process(
        Seq(
          newChange(
            ChangeAction.Modify,
            relations = Seq(
              dataAfter.rawRelationWithId(1)
            )
          ),
          newChange(
            ChangeAction.Delete,
            nodes = Seq(
              newRawNode(1003),
            ),
            ways = Seq(
              newRawWay(102),
            ),
            relations = Seq(
              newRawRelation(12)
            )
          )
        )
      )

      assertBaseNetwork()
      assertNetwork()
      assertRoute1()
      assertRoute2()
      assertNode1003()
      assertNetworkChange()
      assertRouteChange()
      assertNodeChange1002()
      assertNodeChange1003()
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
    networkDoc.routes.map(_.id) should equal(Seq(11L))
  }

  private def assertRoute1(): Unit = {
    val route1 = findRouteById(11)
    route1.active should equal(true)
  }

  private def assertRoute2(): Unit = {
    val route2 = findRouteById(12)
    route2.active should equal(false)
  }

  private def assertNode1003(): Unit = {
    assertEqual(
      findNodeById(1003),
      newNodeDoc(
        1003,
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
      findNetworkChangeById("123:1:1"),
      newNetworkChange(
        key = newChangeKey(elementId = 1),
        networkName = "name",
        changeType = ChangeType.Update,
        country = Some(Country.nl),
        routeType = RouteType.hiking,
        relations = IdDiffs(
          removed = Seq(12)
        ),
        nodeDiffs = RefDiffs(
          removed = Seq(
            Ref(1003, "03")
          ),
          updated = Seq(
            Ref(1002, "02")
          )
        ),
        routeDiffs = RefDiffs(
          removed = Seq(
            Ref(12, "02-03")
          )
        ),
        investigate = true,
        impact = true,
      )
    )
  }

  private def assertRouteChange(): Unit = {
    assertEqual(
      findRouteChangeById("123:1:12"),
      newRouteChange(
        newChangeKey(elementId = 12),
        ChangeType.Delete,
        "02-03",
        removedFromNetwork = Seq(Ref(1, "name")),
        before = Some(
          newRouteData(
            relationId = 12,
            raw = newRaw(
              changeSetId = 1,
              tags = newRouteTags("02-03")
            ),
            countries = Seq(Country.nl),
            routeTypes = Seq(RouteType.hiking),
            name = "02-03",
            networkNodes = Seq(
              newRouteNode(1002, "02"),
              newRouteNode(1003, "03")
            )
          )
        ),
        nodeChanges = Seq(
          newRouteNodeChange(
            1002,
            changeType = ElementChangeType.Removed
          ),
          newRouteNodeChange(
            1003,
            changeType = ElementChangeType.Removed
          )
        ),
        facts = Seq(Fact.Deleted),
        investigate = true,
        impact = true,
        locationInvestigate = true,
        locationImpact = true
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
        removedFromRoute = Seq(Ref(12, "02-03")),
        investigate = true,
        impact = true,
        locationInvestigate = true,
        locationImpact = true
      )
    )
  }

  private def assertNodeChange1003(): Unit = {
    assertEqual(
      findNodeChangeById("123:1:1003"),
      newNodeChange(
        key = newChangeKey(elementId = 1003),
        changeType = ChangeType.Delete,
        subsets = Seq(Subset.nlHiking),
        locations = Seq("nl"),
        name = Some("03"),
        before = Some(
          newMetaData()
        ),
        after = None,
        removedFromRoute = Seq(Ref(12, "02-03")),
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
              routeChanges = ChangeSetElementRefs(
                removed = Seq(newChangeSetElementRef(12, "02-03", investigate = true))
              ),
              nodeChanges = ChangeSetElementRefs(
                removed = Seq(newChangeSetElementRef(1003, "03", investigate = true)),
                updated = Seq(newChangeSetElementRef(1002, "02", investigate = true))
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
                newChangeSetElementRef(1003, "03", investigate = true),
              ),
              updated = Seq(
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
