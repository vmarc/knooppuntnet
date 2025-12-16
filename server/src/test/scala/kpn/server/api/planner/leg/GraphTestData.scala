package kpn.server.api.planner.leg

import kpn.api.common.planner.LegEndRoute
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.core.doc.BaseRouteDoc
import kpn.core.planner.graph.GraphEdge
import kpn.core.planner.graph.NodeNetworkGraphImpl
import kpn.core.test.TestObjects.legEndRoute
import kpn.core.test.TestObjects.newBaseRouteDoc
import kpn.core.test.TestObjects.newRouteBaseData
import kpn.core.test.TestObjects.newRouteInfoAnalysis
import kpn.core.test.TestObjects.newRouteNetworkNodeInfo
import kpn.server.repository.GraphRepository
import kpn.server.repository.RouteRepository
import org.scalamock.stubs.Stubs
import org.scalatest.Assertions.pending

/*
        1m          2m          5m
  n1 ---r1--- n2 ---r2--- n3 ---r3--- n4
   \                     /
    ---------r4---------
             4m
*/
class GraphTestData extends Stubs {

  val node1: RouteNetworkNodeInfo = newRouteNetworkNodeInfo(id = 1001L, name = "01", lat = "1", lon = "1")
  val node2: RouteNetworkNodeInfo = newRouteNetworkNodeInfo(id = 1002L, name = "02", lat = "2", lon = "2")
  val node3: RouteNetworkNodeInfo = newRouteNetworkNodeInfo(id = 1003L, name = "03", lat = "3", lon = "3")
  val node4: RouteNetworkNodeInfo = newRouteNetworkNodeInfo(id = 1004L, name = "04", lat = "4", lon = "4")

  val legEndRoute1: LegEndRoute = legEndRoute(11L, 1L)
  val legEndRoute2: LegEndRoute = legEndRoute(12L, 1L)
  val legEndRoute3: LegEndRoute = legEndRoute(13L, 1L)
  val legEndRoute4: LegEndRoute = legEndRoute(14L, 1L)

  val graphRepository: GraphRepository = {

    val graph = new NodeNetworkGraphImpl()
    graph.add(GraphEdge(node1.id, node2.id, 1, proposed = false, legEndRoute1.trackPathKeys.head))
    graph.add(GraphEdge(node2.id, node3.id, 2, proposed = false, legEndRoute2.trackPathKeys.head))
    graph.add(GraphEdge(node3.id, node4.id, 5, proposed = false, legEndRoute3.trackPathKeys.head))
    graph.add(GraphEdge(node1.id, node3.id, 4, proposed = false, legEndRoute4.trackPathKeys.head))

    val graphRepository = stub[GraphRepository]
    (graphRepository.graph _).returnsWith(Some(graph))

    graphRepository
  }

  val baseRouteRepository: RouteRepository = {
    val repository = stub[RouteRepository]
    //    (routeRepository.findRouteDetailById _).when(legEndRoute1.trackPathKeys.head.routeId).returns(Some(routeDoc(legEndRoute1, node1, node2)))
    //    (routeRepository.findRouteDetailById _).when(legEndRoute2.trackPathKeys.head.routeId).returns(Some(routeDoc(legEndRoute2, node2, node3)))
    //    (routeRepository.findRouteDetailById _).when(legEndRoute3.trackPathKeys.head.routeId).returns(Some(routeDoc(legEndRoute3, node3, node4)))
    //    (routeRepository.findRouteDetailById _).when(legEndRoute4.trackPathKeys.head.routeId).returns(Some(routeDoc(legEndRoute4, node1, node3)))
    repository
  }

  private def routeDoc(legEndRoute: LegEndRoute, startNode: RouteNetworkNodeInfo, endNode: RouteNetworkNodeInfo): BaseRouteDoc = {
    pending
    newBaseRouteDoc(
      legEndRoute.trackPathKeys.head.routeId,
      base = newRouteBaseData(
        analysis = newRouteInfoAnalysis(
          // TODO redesign
          //  map = RouteMap(
          //    startNodes = Seq(startNode),
          //    endNodes = Seq(endNode),
          //    forwardPath = Some(
          //      TrackPath(
          //        pathId = legEndRoute.trackPathKeys.head.pathId,
          //        startNodeId = startNode.id,
          //        endNodeId = endNode.id,
          //        meters = 0,
          //        oneWay = false,
          //        segments = Seq(
          //          TrackSegment(
          //            surface = "unpaved",
          //            source = TrackPoint(startNode.lat, startNode.lon),
          //            fragments = Seq(
          //              TrackSegmentFragment(
          //                trackPoint = TrackPoint(endNode.lat, endNode.lon),
          //                meters = 0
          //              )
          //            )
          //          )
          //        )
          //      )
          //    )
          //  )
        )
      )
    )
  }
}
