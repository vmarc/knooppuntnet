package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.NetworkType
import kpn.core.planner.graph.NodeNetworkGraph
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.GraphRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

class Issue150_RoutingAgainstOneWayDirection extends UnitTest {

  test("bicyle routing against one-way direction") {

    withDatabase(database => {

      val routeRepository = new RouteRepositoryImpl(database)
      val routeAnalysis1 = CaseStudy.routeAnalysis("12410463")
      val routeAnalysis2 = CaseStudy.routeAnalysis("1029893")

      routeRepository.save(routeAnalysis1.route)
      routeRepository.save(routeAnalysis2.route)

      val graphRepository = new GraphRepositoryImpl(database, graphLoadEnabled = true)
      graphRepository.loadGraphs()

      val graph: NodeNetworkGraph = graphRepository.graph(NetworkType.cycling).get

      val startNode = "7741683309" // node 57

      val graphPath = graph.findPath(startNode, "42784896").get

      graphPath.source should equal(startNode)

      val route1forwardPath = routeAnalysis1.route.analysis.map.forwardPath.get
      val route2forwardPath = routeAnalysis2.route.analysis.map.forwardPath.get
      val route2startTentablePath1 = routeAnalysis2.route.analysis.map.startTentaclePaths.head

      val Seq(segment1a, segment1b, segment2a, segment2b, segment3a, segment3b) = graphPath.segments

      segment1a.sink should equal("12410463.101")
      segment1a.pathKey.routeId should equal(routeAnalysis1.id)
      segment1a.pathKey.pathId should equal(route1forwardPath.pathId + 100) // 101 oneWay false

      segment1b.sink should equal("7741672259")
      segment1b.pathKey.routeId should equal(routeAnalysis1.id)
      segment1b.pathKey.pathId should equal(route1forwardPath.pathId + 100) // 101 oneWay false

      // backward (reverse) order:  101
      route1forwardPath.startNodeId should equal(7741672259L) // node 09-57_09.a 09-53_09.b  (node on the right)
      route1forwardPath.endNodeId should equal(7741683309L) // node 57



      segment2a.sink should equal("1029893.3")
      segment2a.pathKey.routeId should equal(routeAnalysis2.id)
      segment2a.pathKey.pathId should equal(route2startTentablePath1.pathId) // 3

      segment2b.sink should equal("7751484678")
      segment2b.pathKey.routeId should equal(routeAnalysis2.id)
      segment2b.pathKey.pathId should equal(route2startTentablePath1.pathId) // 3

      route2startTentablePath1.startNodeId should equal(7741672259L)  // node 09-53_09.b  (node on the right)
      route2startTentablePath1.endNodeId should equal(7751484678L)  // node 09-53_09.a  (node on the left)
      val xx = route2startTentablePath1.segments



      segment3a.sink should equal("1029893.1")
      segment3a.pathKey.routeId should equal(routeAnalysis2.id)
      segment3a.pathKey.pathId should equal(route2forwardPath.pathId) // 1 oneWay true

      segment3b.sink should equal("42784896")
      segment3b.pathKey.routeId should equal(routeAnalysis2.id)
      segment3b.pathKey.pathId should equal(route2forwardPath.pathId) // 1 oneWay true

      route2forwardPath.startNodeId should equal(7751484678L) // node 09-53_09.a  (node on the left)
      route2forwardPath.endNodeId should equal(42784896L) // node 53


      println(graphPath)
    })
  }

}
