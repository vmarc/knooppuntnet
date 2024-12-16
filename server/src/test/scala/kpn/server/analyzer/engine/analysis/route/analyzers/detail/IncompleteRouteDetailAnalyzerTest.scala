package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.api.common.Fact.RouteIncomplete
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteTestData
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

class IncompleteRouteDetailAnalyzerTest extends UnitTest {

  test("route relation without fixme=incomplete tag") {
    val d = new RouteTestData("01-02")
    val context = analyze(d)
    assertEqual(context.facts, Seq.empty)
  }

  test("route relation with fixme=incomplete tag") {
    val d = new RouteTestData("01-02", routeTags = Tags.from("fixme" -> "incomplete"))
    val context = analyze(d)
    assertEqual(context.facts.toSet, Set(RouteIncomplete))
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
