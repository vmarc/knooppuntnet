package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact.RouteWithoutWays
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteTestData
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.context.AnalysisContext

class WithoutWaysRouteAnalyzerTest extends UnitTest {

  test("route relation contains way(s)") {

    val d = new RouteTestData("01-02") {
      node(1001, "01")
      node(1002, "02")
      memberWay(101L, "", 1001, 1002)
    }

    val context = analyze(d)

    context.facts shouldBe empty
  }

  test("route relation without way members") {
    val d = new RouteTestData("01-02")
    val context = analyze(d)
    context.facts should equal(Seq(RouteWithoutWays))
  }

  private def analyze(routeTestData: RouteTestData): RouteAnalysisContext = {
    val relation = routeTestData.data.relations(1L)
    val context = RouteAnalysisContext(new AnalysisContext(), relation)
    WithoutWaysRouteAnalyzer.analyze(context)
  }

}