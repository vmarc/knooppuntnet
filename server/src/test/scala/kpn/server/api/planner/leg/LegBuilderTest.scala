package kpn.server.api.planner.leg

import kpn.api.common.SharedTestObjects
import kpn.api.common.common.TrackPath
import kpn.api.common.common.TrackPathKey
import kpn.api.common.common.TrackPoint
import kpn.api.common.common.TrackSegment
import kpn.api.common.common.TrackSegmentFragment
import kpn.api.common.planner.LegBuildParams
import kpn.api.common.planner.LegEnd
import kpn.api.common.planner.LegEndRoute
import kpn.api.common.planner.RouteLeg
import kpn.api.common.planner.RouteLegFragment
import kpn.api.common.planner.RouteLegNode
import kpn.api.common.planner.RouteLegRoute
import kpn.api.common.planner.RouteLegSegment
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteMap
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.api.custom.NetworkType
import kpn.core.planner.graph.GraphEdge
import kpn.core.planner.graph.GraphPath
import kpn.core.planner.graph.GraphPathSegment
import kpn.core.planner.graph.NodeNetworkGraphImpl
import kpn.core.util.UnitTest
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
class LegBuilderTest extends UnitTest with MockFactory with SharedTestObjects {

  private val node1 = newRouteNetworkNodeInfo(id = 1001L, name = "01", lat = "1", lon = "1")
  private val node2 = newRouteNetworkNodeInfo(id = 1002L, name = "02", lat = "2", lon = "2")
  private val node3 = newRouteNetworkNodeInfo(id = 1003L, name = "03", lat = "3", lon = "3")
  private val node4 = newRouteNetworkNodeInfo(id = 1004L, name = "04", lat = "4", lon = "4")

  private val legEndRoute1 = LegEndRoute(11L, 1L)
  private val legEndRoute2 = LegEndRoute(12L, 1L)
  private val legEndRoute3 = LegEndRoute(13L, 1L)
  private val legEndRoute4 = LegEndRoute(14L, 1L)

  test("node1 to node4") {

    val graphRepository = buildGraphRepository()
    val routeRepository = buildRouteRepository()
    val legBuilder = new LegBuilderImpl(graphRepository, routeRepository)

    val params = LegBuildParams(
      networkType = NetworkType.hiking.name,
      legId = "leg1",
      source = LegEnd.node(node1.id),
      sink = LegEnd.node(node4.id)
    )

    legBuilder.build(params) should equal(
      Some(
        RouteLeg(
          legId = "leg1",
          routes = Seq(
            toRouteLegRoute(node1, node2),
            toRouteLegRoute(node2, node3),
            toRouteLegRoute(node3, node4)
          )
        )
      )
    )
  }

  test("node1 to route3") {

    val graphRepository = buildGraphRepository()
    val routeRepository = buildRouteRepository()
    val legBuilder = new LegBuilderImpl(graphRepository, routeRepository)

    val params = LegBuildParams(
      networkType = NetworkType.hiking.name,
      legId = "leg1",
      source = LegEnd.node(node1.id),
      sink = LegEnd.route(legEndRoute3)
    )

    legBuilder.build(params) should equal(
      Some(
        RouteLeg(
          legId = "leg1",
          routes = Seq(
            toRouteLegRoute(node1, node2),
            toRouteLegRoute(node2, node3),
            toRouteLegRoute(node3, node4)
          )
        )
      )
    )
  }

  test("route1 to route3") {

    val graphRepository = buildGraphRepository()
    val routeRepository = buildRouteRepository()
    val legBuilder = new LegBuilderImpl(graphRepository, routeRepository)

    val params = LegBuildParams(
      networkType = NetworkType.hiking.name,
      legId = "leg1",
      source = LegEnd.route(legEndRoute1),
      sink = LegEnd.route(legEndRoute3)
    )

    legBuilder.build(params) should equal(
      Some(
        RouteLeg(
          legId = "leg1",
          routes = Seq(
            toRouteLegRoute(node1, node2),
            toRouteLegRoute(node2, node3),
            toRouteLegRoute(node3, node4)
          )
        )
      )
    )
  }

  test("node1 to route1") {

    val graphRepository = buildGraphRepository()
    val routeRepository = buildRouteRepository()
    val legBuilder = new LegBuilderImpl(graphRepository, routeRepository)

    val params = LegBuildParams(
      networkType = NetworkType.hiking.name,
      legId = "leg1",
      source = LegEnd.node(node1.id),
      sink = LegEnd.route(legEndRoute1)
    )

    legBuilder.build(params) should equal(
      Some(
        RouteLeg(
          legId = "leg1",
          routes = Seq(
            toRouteLegRoute(node1, node2)
          )
        )
      )
    )
  }

  test("node1 to route4") {

    val graphRepository = buildGraphRepository()
    val routeRepository = buildRouteRepository()
    val legBuilder = new LegBuilderImpl(graphRepository, routeRepository)

    val params = LegBuildParams(
      networkType = NetworkType.hiking.name,
      legId = "leg1",
      source = LegEnd.node(node1.id),
      sink = LegEnd.route(legEndRoute4)
    )

    legBuilder.build(params) should equal(
      Some(
        RouteLeg(
          legId = "leg1",
          routes = Seq(
            toRouteLegRoute(node1, node3)
          )
        )
      )
    )
  }

  private def buildGraphRepository(): GraphRepository = {

    val graph = new NodeNetworkGraphImpl()
    graph.add(GraphEdge(node1.id, node2.id, 1, TrackPathKey(legEndRoute1.routeId, legEndRoute1.pathId)))
    graph.add(GraphEdge(node2.id, node3.id, 2, TrackPathKey(legEndRoute2.routeId, legEndRoute2.pathId)))
    graph.add(GraphEdge(node3.id, node4.id, 5, TrackPathKey(legEndRoute3.routeId, legEndRoute3.pathId)))
    graph.add(GraphEdge(node1.id, node3.id, 4, TrackPathKey(legEndRoute4.routeId, legEndRoute4.pathId)))

    val graphRepository: GraphRepository = stub[GraphRepository]
    (graphRepository.graph _).when(NetworkType.hiking).returns(Some(graph))

    graphRepository
  }

  private def buildRouteRepository(): RouteRepository = {
    val routeRepository = stub[RouteRepository]
    (routeRepository.routeWithId _).when(legEndRoute1.routeId).returns(Some(routeInfo(legEndRoute1, node1, node2)))
    (routeRepository.routeWithId _).when(legEndRoute2.routeId).returns(Some(routeInfo(legEndRoute2, node2, node3)))
    (routeRepository.routeWithId _).when(legEndRoute3.routeId).returns(Some(routeInfo(legEndRoute3, node3, node4)))
    (routeRepository.routeWithId _).when(legEndRoute4.routeId).returns(Some(routeInfo(legEndRoute4, node1, node3)))
    routeRepository
  }

  private def toRouteLegNode(node: RouteNetworkNodeInfo): RouteLegNode = {
    RouteLegNode(
      nodeId = node.id.toString,
      nodeName = node.name,
      lat = node.lat,
      lon = node.lon
    )
  }

  private def graphPath1(): GraphPath = {
    GraphPath(
      node1.id,
      Seq(
        GraphPathSegment(
          sinkNodeId = node2.id,
          pathKey = TrackPathKey(legEndRoute1.routeId, legEndRoute1.pathId)
        )
      )
    )
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

  private def toRouteLegRoute(startNode: RouteNetworkNodeInfo, endNode: RouteNetworkNodeInfo): RouteLegRoute = {

    RouteLegRoute(
      source = toRouteLegNode(startNode),
      sink = toRouteLegNode(endNode),
      meters = 0,
      segments = Seq(
        RouteLegSegment(
          meters = 0,
          surface = "unpaved",
          colour = None,
          fragments = Seq(
            RouteLegFragment(
              lat = endNode.lat,
              lon = endNode.lon,
              meters = 0,
              orientation = 1,
              streetIndex = None
            )
          )
        )
      ),
      streets = Seq()
    )
  }

}
