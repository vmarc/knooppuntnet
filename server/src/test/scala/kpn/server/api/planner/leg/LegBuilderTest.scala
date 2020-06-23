package kpn.server.api.planner.leg

import kpn.api.common.SharedTestObjects
import kpn.api.common.common.TrackPath
import kpn.api.common.common.TrackPathKey
import kpn.api.common.common.TrackPoint
import kpn.api.common.common.TrackSegment
import kpn.api.common.common.TrackSegmentFragment
import kpn.api.common.planner.LegBuildParams
import kpn.api.common.planner.LegEnd
import kpn.api.common.planner.RouteLeg
import kpn.api.common.planner.RouteLegFragment
import kpn.api.common.planner.RouteLegNode
import kpn.api.common.planner.RouteLegRoute
import kpn.api.common.planner.RouteLegSegment
import kpn.api.common.route.RouteMap
import kpn.api.custom.NetworkType
import kpn.core.planner.graph.GraphPath
import kpn.core.planner.graph.GraphPathSegment
import kpn.core.planner.graph.NodeNetworkGraph
import kpn.core.util.UnitTest
import kpn.server.repository.GraphRepository
import kpn.server.repository.RouteRepository
import org.scalamock.scalatest.MockFactory

class LegBuilderTest extends UnitTest with MockFactory with SharedTestObjects {

  test("node to node") {

    val graphPath1 = GraphPath(
      1001,
      Seq(
        GraphPathSegment(
          sinkNodeId = 1002,
          pathKey = TrackPathKey(10, 1)
        )
      )
    )

    val graph = stub[NodeNetworkGraph]
    (graph.findPath _).when(1001, 1002).returns(Some(graphPath1))

    val graphRepository: GraphRepository = stub[GraphRepository]
    (graphRepository.graph _).when(NetworkType.hiking).returns(Some(graph))

    val routeInfo = newRouteInfo(
      summary = newRouteSummary(
        id = 10
      ),
      analysis = newRouteInfoAnalysis(
        map = RouteMap(
          startNodes = Seq(
            newRouteNetworkNodeInfo(
              id = 1001,
              name = "01",
              lat = "1",
              lon = "1"
            )
          ),
          endNodes = Seq(
            newRouteNetworkNodeInfo(
              id = 1002,
              name = "02",
              lat = "2",
              lon = "2"
            )
          ),
          forwardPath = Some(
            TrackPath(
              pathId = 1,
              startNodeId = 1001,
              endNodeId = 1002,
              meters = 0,
              oneWay = false,
              segments = Seq(
                TrackSegment(
                  surface = "unpaved",
                  source = TrackPoint("1", "1"),
                  fragments = Seq(
                    TrackSegmentFragment(
                      trackPoint = TrackPoint("2", "2"),
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

    val routeRepository = stub[RouteRepository]
    (routeRepository.routeWithId _).when(10).returns(Some(routeInfo))

    val legBuilder = new LegBuilderImpl(graphRepository, routeRepository)

    val params = new LegBuildParams(
      networkType = NetworkType.hiking.name,
      legId = "leg1",
      source = LegEnd.node(1001),
      sink = LegEnd.node(1002)
    )

    legBuilder.build(params) should equal(
      Some(
        RouteLeg(
          legId = "leg1",
          routes = Seq(
            RouteLegRoute(
              source = RouteLegNode(
                nodeId = "1001",
                nodeName = "01",
                lat = "1",
                lon = "1"
              ),
              sink = RouteLegNode(
                nodeId = "1002",
                nodeName = "02",
                lat = "2",
                lon = "2"
              ),
              meters = 0,
              segments = Seq(
                RouteLegSegment(
                  meters = 0,
                  surface = "unpaved",
                  colour = None,
                  fragments = Seq(
                    RouteLegFragment(
                      lat = "2",
                      lon = "2",
                      meters = 0,
                      orientation = 1,
                      streetIndex = None
                    )
                  )
                )
              ),
              streets = Seq()
            )
          )
        )
      )
    )
  }

}
