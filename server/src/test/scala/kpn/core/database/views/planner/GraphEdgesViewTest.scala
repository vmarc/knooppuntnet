package kpn.core.database.views.planner

import kpn.core.database.Database
import kpn.core.planner.graph.GraphEdge
import kpn.core.test.TestSupport.withDatabase
import kpn.server.repository.RouteRepositoryImpl
import kpn.shared.NetworkType
import kpn.shared.RouteSummary
import kpn.shared.Timestamp
import kpn.shared.common._
import kpn.shared.data.Tags
import kpn.shared.route.RouteInfo
import kpn.shared.route.RouteInfoAnalysis
import kpn.shared.route.RouteMap
import org.scalatest.FunSuite
import org.scalatest.Matchers

class GraphEdgesViewTest extends FunSuite with Matchers {

  private val routeId = 10L

  private val nodeId1 = 1001L
  private val nodeId2 = 1002L
  private val nodeId3 = 1003L
  private val nodeId4 = 1004L

  private val trackPoint1 = TrackPoint("1", "1")
  private val trackPoint2 = TrackPoint("2", "2")
  private val trackPoint3 = TrackPoint("3", "3")
  private val trackPoint4 = TrackPoint("4", "4")

  private val path1 = TrackPath(nodeId1, nodeId2, 100, Seq(TrackSegment("", trackPoint1, Seq(TrackSegmentFragment(trackPoint2, 0, 0, None)))))
  private val path2 = TrackPath(nodeId3, nodeId4, 200, Seq(TrackSegment("", trackPoint3, Seq(TrackSegmentFragment(trackPoint4, 0, 0, None)))))

  test("graph edge forward path") {
    withDatabase { database =>
      doTest(database, RouteMap(forwardPath = Some(path1))) should equal(
        Seq(
          GraphEdge(nodeId1, nodeId2, 100, TrackPathKey(routeId, "forward", 1))
        )
      )
    }
  }

  test("graph edge backward path") {
    withDatabase { database =>
      doTest(database, RouteMap(backwardPath = Some(path1))) should equal(
        Seq(
          GraphEdge(nodeId1, nodeId2, 100, TrackPathKey(routeId, "backward", 1))
        )
      )
    }
  }

  test("graph edge start tentacle path") {
    withDatabase { database =>
      doTest(database, RouteMap(startTentaclePaths = Seq(path1, path2))) should equal(
        Seq(
          GraphEdge(nodeId1, nodeId2, 100, TrackPathKey(routeId, "start", 1)),
          GraphEdge(nodeId3, nodeId4, 200, TrackPathKey(routeId, "start", 2))
        )
      )
    }
  }

  test("graph edge forward tentacle path") {
    withDatabase { database =>
      doTest(database, RouteMap(endTentaclePaths = Seq(path1, path2))) should equal(
        Seq(
          GraphEdge(nodeId1, nodeId2, 100, TrackPathKey(routeId, "end", 1)),
          GraphEdge(nodeId3, nodeId4, 200, TrackPathKey(routeId, "end", 2))
        )
      )
    }
  }

  private def doTest(database: Database, routeMap: RouteMap): Seq[GraphEdge] = {
    val routeRepository = new RouteRepositoryImpl(database)
    val routeInfo = buildRoute(routeMap)
    routeRepository.save(routeInfo)
    GraphEdgesView.query(database, NetworkType.hiking, stale = false)
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
      tags = Tags.empty,
      facts = Seq(),
      analysis = Some(
        RouteInfoAnalysis(
          startNodes = Seq(),
          endNodes = Seq(),
          startTentacleNodes = Seq(),
          endTentacleNodes = Seq(),
          unexpectedNodeIds = Seq(),
          members = Seq(),
          expectedName = "",
          map = routeMap,
          structureStrings = Seq(),
          locationAnalysis = None
        )
      )
    )
  }
}
