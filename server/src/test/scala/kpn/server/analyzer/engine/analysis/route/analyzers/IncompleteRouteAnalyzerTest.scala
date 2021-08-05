package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact.RouteIncomplete
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteTestData
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.context.AnalysisContext

class IncompleteRouteAnalyzerTest extends UnitTest {

  test("route relation without fixme=incomplete tag") {
    val d = new RouteTestData("01-02")
    val context = analyze(d)
    context.facts shouldBe empty
  }

  test("route relation with fixme=incomplete tag") {
    val d = new RouteTestData("01-02", routeTags = Tags.from("fixme" -> "incomplete"))
    val context = analyze(d)
    context.facts should equal(Seq(RouteIncomplete))
  }

  private def analyze(routeTestData: RouteTestData): RouteAnalysisContext = {
    val relation = routeTestData.data.relations(1L)
    val context = RouteAnalysisContext(
      new AnalysisContext(),
      relation
    )
    IncompleteRouteAnalyzer.analyze(context)
  }

}