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

class NetworkUpdateRouteTest05 extends IntegrationTest {

  test("network update - an orphan route that is added to the network is no longer orphan") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route( // this orphan route is not referenced by the network
        11,
        "01-02",
        Seq(
          newMember(MemberType.Way, 101)
        )
      )
      .networkRelation(
        1,
        "name",
        Seq(
          newMember(MemberType.Node, 1001),
          newMember(MemberType.Node, 1002)
          // the network does not reference the route
        )
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route( // the route definition itself has not changed
        11,
        "01-02",
        Seq(
          newMember(MemberType.Way, 101)
        )
      )
      .networkRelation(
        1,
        "name",
        Seq(
          newMember(MemberType.Node, 1001),
          newMember(MemberType.Node, 1002),
          newMember(MemberType.Relation, 11) // route is now part of the network
        )
      )

    testIntegration(dataBefore, dataAfter) {

      assertOrphanRouteBefore()

      process(ChangeAction.Modify, dataAfter.rawRelationWithId(1))

      watched.routes.ids should contain(11)
      database.nodeChanges shouldBe empty
      database.orphanRoutes shouldBe empty

      assertBaseNetwork()
      assertNetwork()
      assertNetworkChange()
      assertRouteChange()
      assertChangeSetSummary()
    }
  }

  private def assertOrphanRouteBefore(): Unit = {
    assertEqual(
      findOrphanRouteById(11),
      newOrphanRouteDoc(
        11L,
        Country.nl,
        RouteType.hiking,
        "01-02"
      )
    )
  }

  private def assertBaseNetwork(): Unit = {
    val baseNetworkDoc = findBaseNetworkById(1)
    baseNetworkDoc._id should equal(1)
  }

  private def assertNetwork(): Unit = {
    val networkDoc = findNetworkById(1)
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
        relations = IdDiffs(
          added = Seq(11)
        ),
        routeDiffs = RefDiffs(
          added = Seq(
            Ref(11, "01-02")
          )
        ),
        happy = true,
        impact = true,
      )
    )
  }

  private def assertRouteChange(): Unit = {
    val routeData = newRouteData(
      relationId = 11,
      meta = newMetaData(changeSetId = 1),
      countries = Seq(Country.nl),
      routeTypes = Seq(RouteType.hiking),
      name = "01-02",
      networkNodes = Seq(
        newRouteNode(1001, "01"),
        newRouteNode(1002, "02")
      ),
      tags = newRouteTags("01-02")
    )

    assertEqual(
      findRouteChangeById("123:1:11"),
      newRouteChange(
        newChangeKey(elementId = 11),
        ChangeType.Update,
        "01-02",
        addedToNetwork = Seq(
          Ref(1, "name")
        ),
        before = Some(routeData),
        after = Some(routeData),
        nodeChanges = Seq(
          newRouteNodeChange(1001),
          newRouteNodeChange(1002)
        ),
        happy = true,
        impact = true,
        locationHappy = true,
        locationImpact = true
      )
    )
  }

  private def assertChangeSetSummary(): Unit = {
    assertEqual(
      findChangeSetSummaryById("123:1"),
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
        networkChanges = NetworkChanges(
          updates = Seq(
            newChangeSetNetwork(
              Some(Country.nl),
              RouteType.hiking,
              1,
              "name",
              routeChanges = ChangeSetElementRefs(
                added = Seq(newChangeSetElementRef(11, "01-02", happy = true))
              ),
              happy = true
            )
          )
        ),
        subsetAnalyses = Seq(
          ChangeSetSubsetAnalysis(Subset.nlHiking, happy = true)
        ),
        happy = true
      )
    )
  }
}
