package kpn.core.database.views.planner

import kpn.api.common.RouteSummary
import kpn.api.common.common._
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteMap
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.TestObjects
import kpn.core.database.Database
import kpn.core.planner.graph.GraphEdge
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.RouteRepositoryImpl

class GraphEdgesViewTest extends UnitTest with TestObjects {

  private val routeId = 10L

  private val nodeId1 = 1001L
  private val nodeId2 = 1002L
  private val nodeId3 = 1003L
  private val nodeId4 = 1004L

  private val trackPoint1 = TrackPoint("1", "1")
  private val trackPoint2 = TrackPoint("2", "2")
  private val trackPoint3 = TrackPoint("3", "3")
  private val trackPoint4 = TrackPoint("4", "4")

  private val path1 = TrackPath(1, nodeId1, nodeId2, 100, oneWay = false, Seq(TrackSegment("", trackPoint1, Seq(TrackSegmentFragment(trackPoint2, 0, 0, None)))))
  private val path2 = TrackPath(2, nodeId3, nodeId4, 200, oneWay = false, Seq(TrackSegment("", trackPoint3, Seq(TrackSegmentFragment(trackPoint4, 0, 0, None)))))

  test("graph edge forward path") {
    withDatabase { database =>
      doTest(database, RouteMap(forwardPath = Some(path1.copy(oneWay = true)))) should equal(
        Set(
          GraphEdge(nodeId1, nodeId2, 100, TrackPathKey(routeId, 1))
        )
      )
    }
  }

  test("graph edge backward path") {
    withDatabase { database =>
      doTest(database, RouteMap(backwardPath = Some(path1.copy(oneWay = true)))) should equal(
        Set(
          GraphEdge(nodeId1, nodeId2, 100, TrackPathKey(routeId, 1))
        )
      )
    }
  }

  test("graph edge start tentacle path") {
    withDatabase { database =>
      doTest(database, RouteMap(startTentaclePaths = Seq(path1.copy(oneWay = true), path2))) should equal(
        Set(
          GraphEdge(nodeId1, nodeId2, 100, TrackPathKey(routeId, 1)),
          GraphEdge(nodeId3, nodeId4, 200, TrackPathKey(routeId, 2)),
          GraphEdge(nodeId4, nodeId3, 200, TrackPathKey(routeId, 102))
        )
      )
    }
  }

  test("graph edge end tentacle path") {
    withDatabase { database =>
      doTest(database, RouteMap(endTentaclePaths = Seq(path1, path2.copy(oneWay = true)))) should equal(
        Set(
          GraphEdge(nodeId1, nodeId2, 100, TrackPathKey(routeId, 1)),
          GraphEdge(nodeId3, nodeId4, 200, TrackPathKey(routeId, 2)),
          GraphEdge(nodeId2, nodeId1, 100, TrackPathKey(routeId, 101))
        )
      )
    }
  }

  private def doTest(database: Database, routeMap: RouteMap): Set[GraphEdge] = {
    val routeRepository = new RouteRepositoryImpl(database)
    val routeInfo = buildRoute(routeMap)
    routeRepository.save(routeInfo)
    GraphEdgesView.query(database, NetworkType.hiking, stale = false).toSet
  }

  private def buildRoute(routeMap: RouteMap): RouteInfo = {
    val summary = RouteSummary(
      id = routeId,
      country = None,
      networkType = NetworkType.hiking,
      name = "",
      meters = 100,
      isBroken = false,
      wayCount = 1,
      timestamp = Timestamp(2018, 8, 11),
      nodeNames = Seq(),
      tags = Tags.empty
    )

    RouteInfo(
      summary = summary,
      active = true,
      orphan = false,
      version = 1,
      changeSetId = 1,
      lastUpdated = Timestamp(2018, 8, 11),
      lastSurvey = None,
      tags = Tags.empty,
      facts = Seq(),
      analysis = newRouteInfoAnalysis(map = routeMap),
      Seq()
    )
  }
}
