package kpn.server.analyzer.engine.changes.integration

import kpn.api.common
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.NetworkChanges
import kpn.api.common.RouteMemberInfo
import kpn.api.common.RouteMemberInfoWay
import kpn.api.common.RouteType
import kpn.api.common.changes.ChangeAction
import kpn.api.common.common.Ref
import kpn.api.common.data.MemberType
import kpn.api.common.data.MetaData
import kpn.api.common.diff.NetworkData
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.common.route.RouteEdge
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.api.common.route.WayDirection
import kpn.api.custom.Subset
import kpn.api.custom.Timestamp
import kpn.core.doc.Label
import kpn.core.test.OverpassData
import kpn.server.analyzer.engine.context.ElementIds

class NetworkUpdateRouteTest01 extends IntegrationTest {

  test("network update - route that is no longer part of the network after update, becomes orphan route if also not referenced in any other network") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(
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
          newMember(MemberType.Relation, 11)
        ),
        version = 1
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route( // route still exists
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
          // route member is no longer included here
        ),
        version = 2
      )

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Modify, dataAfter.rawRelationWithId(1))

      assert(watched.routes.contains(11))

      assertRoute()
      assertRouteDetail()
      assertOrphanRoute()
      assertNetworkInfoChange()
      assertRouteChange()
      assertChangeSetSummary()

      assert(database.nodeChanges.isEmpty)
    }
  }

  private def assertRoute(): Unit = {
    pending // TODO redesign
    assertEqual(
      findRouteById(11),
      newRouteDoc(
        newRouteSummary(
          11,
          name = "01-02",
          countries = Seq(Country.nl),
          wayCount = 1,
          tags = newRouteTags("01-02")
        ),
        labels = Seq(
          Label.active,
          Label.country(Country.nl),
          Label.routeType(RouteType.hiking)
        ),
        members = Seq(
          RouteMemberInfo(
            101,
            MemberType.Way,
            None,
            None,
            None,
            Some(
              RouteMemberInfoWay(
                None,
                Seq(
                  RouteNetworkNodeInfo(1001, "01", "01", None, "0", "0"),
                  RouteNetworkNodeInfo(1002, "02", "02", None, "0", "0")
                ),
                "1",
                1002,
                "2",
                1001,
                Timestamp(2015, 8, 11, 0, 0, 0),
                accessible = true,
                0,
                "2",
                WayDirection.Both,
                Seq.empty,
                newLink() // "wn000"
              )
            )
          )
        ),
        analysis = newRouteInfoAnalysis(
          expectedName = "01-02",
          // TODO redesign
          //  map = newRouteMap(
          //    bounds = MapBounds("0.0", "0.0", "0.0", "0.0"),
          //    forwardPath = Some(
          //      TrackPath(
          //        pathId = 1,
          //        startNodeId = 1001,
          //        endNodeId = 1002,
          //        meters = 0,
          //        oneWay = false,
          //        segments = Seq(
          //          TrackSegment(
          //            "paved",
          //            TrackPoint("0", "0"),
          //            Seq(
          //              TrackSegmentFragment(TrackPoint("0", "0"), 0)
          //            )
          //          )
          //        )
          //      )
          //    ),
          //    backwardPath = Some(
          //      TrackPath(
          //        pathId = 2,
          //        startNodeId = 1002,
          //        endNodeId = 1001,
          //        meters = 0,
          //        oneWay = false,
          //        segments = Seq(
          //          TrackSegment(
          //            "paved",
          //            TrackPoint("0", "0"),
          //            Seq(
          //              TrackSegmentFragment(TrackPoint("0", "0"), 0)
          //            )
          //          )
          //        )
          //      )
          //    ),
          //    startNodes = Seq(
          //      RouteNetworkNodeInfo(1001, "01", "01", None, "0", "0")
          //    ),
          //    endNodes = Seq(
          //      RouteNetworkNodeInfo(1002, "02", "02", None, "0", "0")
          //    )
          //  ),
        )
      )
    )
  }

  private def assertRouteDetail(): Unit = {
    pending // TODO redesign
    assertEqual(
      findBaseRouteById(11),
      newBaseRouteDoc(
        newRouteSummary(
          11,
          name = "01-02",
          countries = Seq(Country.nl),
          wayCount = 1,
          tags = newRouteTags("01-02")
        ),
        labels = Seq(
          Label.active,
          Label.country(Country.nl),
          Label.routeType(RouteType.hiking)
        ),
        members = Seq(
          common.RouteMemberInfo(
            101,
            MemberType.Way,
            None,
            None,
            None,
            Some(
              RouteMemberInfoWay(
                None,
                Seq(
                  RouteNetworkNodeInfo(1001, "01", "01", None, "0", "0"),
                  RouteNetworkNodeInfo(1002, "02", "02", None, "0", "0")
                ),
                "1",
                1002,
                "2",
                1001,
                Timestamp(2015, 8, 11, 0, 0, 0),
                accessible = true,
                0,
                "2",
                WayDirection.Both,
                Seq.empty,
                newLink() // "wn000"
              )
            )
          )
        ),
        analysis = newRouteInfoAnalysis(
          expectedName = "01-02",
          // TODO redesign
          //  map = newRouteMap(
          //    bounds = MapBounds("0.0", "0.0", "0.0", "0.0"),
          //    forwardPath = Some(
          //      TrackPath(
          //        pathId = 1,
          //        startNodeId = 1001,
          //        endNodeId = 1002,
          //        meters = 0,
          //        oneWay = false,
          //        segments = Seq(
          //          TrackSegment(
          //            "paved",
          //            TrackPoint("0", "0"),
          //            Seq(
          //              TrackSegmentFragment(TrackPoint("0", "0"), 0)
          //            )
          //          )
          //        )
          //      )
          //    ),
          //    backwardPath = Some(
          //      TrackPath(
          //        pathId = 2,
          //        startNodeId = 1002,
          //        endNodeId = 1001,
          //        meters = 0,
          //        oneWay = false,
          //        segments = Seq(
          //          TrackSegment(
          //            "paved",
          //            TrackPoint("0", "0"),
          //            Seq(
          //              TrackSegmentFragment(TrackPoint("0", "0"), 0)
          //            )
          //          )
          //        )
          //      )
          //    ),
          //    startNodes = Seq(
          //      RouteNetworkNodeInfo(1001, "01", "01", None, "0", "0")
          //    ),
          //    endNodes = Seq(
          //      RouteNetworkNodeInfo(1002, "02", "02", None, "0", "0")
          //    )
          //  ),
        ),
        nodeRefs = Seq(
          1001,
          1002
        ),
        elementIds = ElementIds(
          nodeIds = Set(1001, 1002),
          wayIds = Set(101)
        ),
        edges = Seq(
          RouteEdge(1, 1001, 1002, 0),
          RouteEdge(101, 1002, 1001, 0),
          RouteEdge(2, 1002, 1001, 0),
          RouteEdge(102, 1001, 1002, 0)
        )
      )
    )
  }

  private def assertOrphanRoute(): Unit = {
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

  private def assertNetworkInfoChange(): Unit = {
    assertEqual(
      findNetworkInfoChangeById("123:1:1"),
      newNetworkInfoChange(
        newChangeKey(elementId = 1),
        ChangeType.Update,
        Some(Country.nl),
        RouteType.hiking,
        1,
        "name",
        networkDataUpdate = Some(
          NetworkDataUpdate(
            Some(
              NetworkData(
                MetaData(1, defaultTimestamp, 1),
                name = "name"
              )
            ),
            Some(
              NetworkData(
                MetaData(2, defaultTimestamp, 1),
                name = "name"
              )
            ),
          )
        ),
        routeDiffs = RefDiffs(
          removed = Seq(
            Ref(11, "01-02")
          )
        ),
        investigate = true
      )
    )
  }

  private def assertRouteChange(): Unit = {

    pending // TODO redesign
    val routeData = newRouteData()
    //  val routeData = newRouteData(
    //    Some(Country.nl),
    //    routeType.hiking,
    //    relation = newRawRelation(
    //      11,
    //      members = Seq(
    //        RawMember("way", 101, None)
    //      ),
    //      tags = newRouteTags("01-02")
    //    ),
    //    name = "01-02",
    //    networkNodes = Seq(
    //      newNodeWithName(1001, "01"),
    //      newNodeWithName(1002, "02")
    //    ),
    //    nodes = Seq(
    //      newNodeWithName(1001, "01"),
    //      newNodeWithName(1002, "02")
    //    ),
    //    ways = Seq(
    //      newRawWay(
    //        101,
    //        nodeIds = Vector(1001, 1002),
    //        tags = Tags.from("highway" -> "unclassified")
    //      )
    //    )
    //  )

    assertEqual(
      findRouteChangeById("123:1:11"),
      newRouteChange(
        newChangeKey(elementId = 11),
        ChangeType.Update,
        "01-02",
        removedFromNetwork = Seq(Ref(1, "name")),
        before = Some(routeData),
        after = Some(routeData),
        impactedNodeIds = Seq(1001, 1002),
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
          updates = Seq(
            newChangeSetNetwork(
              Some(Country.nl),
              RouteType.hiking,
              1,
              "name",
              routeChanges = ChangeSetElementRefs(
                removed = Seq(
                  newChangeSetElementRef(11, "01-02", investigate = true)
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
}
