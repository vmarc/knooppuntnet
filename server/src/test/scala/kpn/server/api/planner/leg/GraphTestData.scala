package kpn.server.api.planner.leg

import kpn.api.common.SharedTestObjects
import kpn.api.common.common.TrackPath
import kpn.api.common.common.TrackPoint
import kpn.api.common.common.TrackSegment
import kpn.api.common.common.TrackSegmentFragment
import kpn.api.common.planner.LegEndRoute
import kpn.api.common.route.RouteMap
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.api.custom.NetworkType
import kpn.core.doc.RouteDoc
import kpn.core.planner.graph.GraphEdge
import kpn.core.planner.graph.NodeNetworkGraphImpl
import kpn.server.repository.GraphRepository
import kpn.server.repository.RouteRepository
import org.scalamock.scalatest.MockFactory

/*
        1m          2m          5m
  n1 ---r1--- n2 ---r2--- n3 ---r3--- n4
   \                     /
    ---------r4---------
             4m
*/
class GraphTestData extends SharedTestObjects with MockFactory {

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

    val graphRepository: GraphRepository = stub[GraphRepository]
    (graphRepository.graph _).when(NetworkType.hiking).returns(Some(graph))

    graphRepository
  }

  val routeRepository: RouteRepository = {
    val routeRepository = stub[RouteRepository]
    (routeRepository.findById _).when(legEndRoute1.trackPathKeys.head.routeId).returns(Some(routeDoc(legEndRoute1, node1, node2)))
    (routeRepository.findById _).when(legEndRoute2.trackPathKeys.head.routeId).returns(Some(routeDoc(legEndRoute2, node2, node3)))
    (routeRepository.findById _).when(legEndRoute3.trackPathKeys.head.routeId).returns(Some(routeDoc(legEndRoute3, node3, node4)))
    (routeRepository.findById _).when(legEndRoute4.trackPathKeys.head.routeId).returns(Some(routeDoc(legEndRoute4, node1, node3)))
    routeRepository
  }

  private def routeDoc(legEndRoute: LegEndRoute, startNode: RouteNetworkNodeInfo, endNode: RouteNetworkNodeInfo): RouteDoc = {
    newRouteDoc(
      summary = newRouteSummary(
        id = legEndRoute.trackPathKeys.head.routeId
      ),
      analysis = newRouteInfoAnalysis(
        map = RouteMap(
          startNodes = Seq(startNode),
          endNodes = Seq(endNode),
          forwardPath = Some(
            TrackPath(
              pathId = legEndRoute.trackPathKeys.head.pathId,
              startNodeId = startNode.id,
              endNodeId = endNode.id,
              meters = 0,
              oneWay = false,
              segments = Seq(
                TrackSegment(
                  surface = "unpaved",
                  source = TrackPoint(startNode.lat, startNode.lon),
                  fragments = Seq(
                    TrackSegmentFragment(
                      trackPoint = TrackPoint(endNode.lat, endNode.lon),
                      meters = 0,
                      orientation = 1,
                      streetIndex = None
                    )
                  )
                )
              )
            )
          )
        )
      )
    )
  }
}
