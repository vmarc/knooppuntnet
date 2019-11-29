package kpn.server.analyzer.engine.analysis.route

import kpn.api.custom.Fact.RouteBroken
import kpn.api.custom.Fact.RouteWithoutWays
import kpn.api.custom.NetworkType
import kpn.server.analyzer.engine.analysis.route.analyzers.AccessibilityAnalyzerImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileAnalyzerImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.load.data.LoadedRoute
import org.scalatest.FunSuite
import org.scalatest.Matchers

class RouteAnalyzerRouteWithoutWaysTest extends FunSuite with Matchers {

  test("RouteNotForward and RouteNotBackward should not be reported for routes without ways") {

    val data = new RouteTestData("01-02") {
      node(1, "01")
      node(2, "02")
      memberNode(1)
      memberNode(2)
    }.data

    val loadedRoute = LoadedRoute(
      country = None,
      networkType = NetworkType.hiking,
      "01-02",
      data,
      data.relations(1L)
    )

    val analysisContext = new AnalysisContext()
    val tileCalculator = new TileCalculatorImpl()
    val routeTileAnalyzer = new RouteTileAnalyzerImpl(tileCalculator)
    val routeAnalysis = new MasterRouteAnalyzerImpl(analysisContext, new AccessibilityAnalyzerImpl(), routeTileAnalyzer).analyze(Map(), loadedRoute, orphan = false)
    routeAnalysis.route.facts should equal(Seq(RouteWithoutWays, RouteBroken))
  }
}
