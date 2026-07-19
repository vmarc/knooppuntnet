package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact.RouteIncomplete
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteTestData

class BaseRouteIncompleteAnalyzerTest extends UnitTest {

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

  private def analyze(routeTestData: RouteTestData): BaseRouteAnalysisContext = {
    val relation = routeTestData.data.relations(1L)
    val context = BaseRouteAnalysisContext(
      relation,
      None
    )
    BaseRouteIncompleteAnalyzer.analyze(context)
  }
}
