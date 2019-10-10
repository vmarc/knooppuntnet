package kpn.core.engine.analysis.route

import kpn.core.engine.analysis.RouteTestData
import kpn.core.engine.analysis.route.analyzers.AccessibilityAnalyzerImpl
import kpn.core.load.data.LoadedRoute
import kpn.core.tools.analyzer.AnalysisContext
import kpn.shared.Fact.RouteBroken
import kpn.shared.Fact.RouteWithoutWays
import kpn.shared.NetworkType
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
    val routeAnalysis = new MasterRouteAnalyzerImpl(analysisContext, new AccessibilityAnalyzerImpl()).analyze(Map(), loadedRoute, orphan = false)
    routeAnalysis.route.facts should equal(Seq(RouteWithoutWays, RouteBroken))
  }
}
