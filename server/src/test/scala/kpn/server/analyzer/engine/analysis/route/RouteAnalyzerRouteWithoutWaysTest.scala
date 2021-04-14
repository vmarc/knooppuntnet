package kpn.server.analyzer.engine.analysis.route

import kpn.api.custom.Fact.RouteBroken
import kpn.api.custom.Fact.RouteWithoutWays
import kpn.api.custom.ScopedNetworkType
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteNodeInfoAnalyzerImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileAnalyzerImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.load.data.LoadedRoute

class RouteAnalyzerRouteWithoutWaysTest extends UnitTest {

  test("RouteNotForward and RouteNotBackward should not be reported for routes without ways") {

    val data = new RouteTestData("01-02") {
      node(1, "01")
      node(2, "02")
      memberNode(1)
      memberNode(2)
    }.data

    val loadedRoute = LoadedRoute(
      country = None,
      scopedNetworkType = ScopedNetworkType.rwn,
      data,
      data.relations(1L)
    )

    val analysisContext = new AnalysisContext()
    val tileCalculator = new TileCalculatorImpl()
    val routeTileAnalyzer = new RouteTileAnalyzerImpl(tileCalculator)
    val routeLocationAnalyzer = new RouteLocationAnalyzerMock()
    val nodeAnalyzer = new NodeAnalyzerImpl()
    val routeNodeInfoAnalyzer = new RouteNodeInfoAnalyzerImpl(nodeAnalyzer)
    val routeAnalyzer = new MasterRouteAnalyzerImpl(
      analysisContext,
      routeLocationAnalyzer,
      routeTileAnalyzer,
      routeNodeInfoAnalyzer
    )
    val routeAnalysis = routeAnalyzer.analyze(loadedRoute, orphan = false)
    routeAnalysis.route.facts should equal(Seq(RouteWithoutWays, RouteBroken))
  }
}
