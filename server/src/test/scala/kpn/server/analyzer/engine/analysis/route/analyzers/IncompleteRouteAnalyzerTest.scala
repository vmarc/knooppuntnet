package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact.RouteIncomplete
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteTestData
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

class IncompleteRouteAnalyzerTest extends UnitTest {

  test("route relation without fixme=incomplete tag") {
    val d = new RouteTestData("01-02")
    val context = analyze(d)
    context.facts.shouldMatchTo(Seq.empty)
  }

  test("route relation with fixme=incomplete tag") {
    val d = new RouteTestData("01-02", routeTags = Tags.from("fixme" -> "incomplete"))
    val context = analyze(d)
    context.facts.shouldMatchTo(Seq(RouteIncomplete))
  }

  private def analyze(routeTestData: RouteTestData): RouteDetailAnalysisContext = {
    val relation = routeTestData.data.relations(1L)
    val context = RouteDetailAnalysisContext(
      relation,
      None
    )
    IncompleteRouteAnalyzer.analyze(context)
  }
}
