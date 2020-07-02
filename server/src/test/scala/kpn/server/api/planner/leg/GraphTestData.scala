package kpn.server.api.planner.leg

import kpn.api.common.SharedTestObjects
import kpn.api.common.common.TrackPath
import kpn.api.common.common.TrackPathKey
import kpn.api.common.common.TrackPoint
import kpn.api.common.common.TrackSegment
import kpn.api.common.common.TrackSegmentFragment
import kpn.api.common.planner.LegEndRoute
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteMap
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.api.custom.NetworkType
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

  val legEndRoute1: LegEndRoute = LegEndRoute(11L, 1L)
  val legEndRoute2: LegEndRoute = LegEndRoute(12L, 1L)
  val legEndRoute3: LegEndRoute = LegEndRoute(13L, 1L)
  val legEndRoute4: LegEndRoute = LegEndRoute(14L, 1L)

  val graphRepository: GraphRepository = {

    val graph = new NodeNetworkGraphImpl()
    graph.add(GraphEdge(node1.id, node2.id, 1, TrackPathKey(legEndRoute1.routeId, legEndRoute1.pathId)))
    graph.add(GraphEdge(node2.id, node3.id, 2, TrackPathKey(legEndRoute2.routeId, legEndRoute2.pathId)))
    graph.add(GraphEdge(node3.id, node4.id, 5, TrackPathKey(legEndRoute3.routeId, legEndRoute3.pathId)))
    graph.add(GraphEdge(node1.id, node3.id, 4, TrackPathKey(legEndRoute4.routeId, legEndRoute4.pathId)))

    val graphRepository: GraphRepository = stub[GraphRepository]
    (graphRepository.graph _).when(NetworkType.hiking).returns(Some(graph))

    graphRepository
  }

  val routeRepository: RouteRepository = {
    val routeRepository = stub[RouteRepository]
    (routeRepository.routeWithId _).when(legEndRoute1.routeId).returns(Some(routeInfo(legEndRoute1, node1, node2)))
    (routeRepository.routeWithId _).when(legEndRoute2.routeId).returns(Some(routeInfo(legEndRoute2, node2, node3)))
    (routeRepository.routeWithId _).when(legEndRoute3.routeId).returns(Some(routeInfo(legEndRoute3, node3, node4)))
    (routeRepository.routeWithId _).when(legEndRoute4.routeId).returns(Some(routeInfo(legEndRoute4, node1, node3)))
    routeRepository
  }

  private def routeInfo(legEndRoute: LegEndRoute, startNode: RouteNetworkNodeInfo, endNode: RouteNetworkNodeInfo): RouteInfo = {
    newRouteInfo(
      summary = newRouteSummary(
        id = legEndRoute.routeId
      ),
      analysis = newRouteInfoAnalysis(
        map = RouteMap(
          startNodes = Seq(startNode),
          endNodes = Seq(endNode),
          forwardPath = Some(
            TrackPath(
              pathId = legEndRoute.pathId,
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
