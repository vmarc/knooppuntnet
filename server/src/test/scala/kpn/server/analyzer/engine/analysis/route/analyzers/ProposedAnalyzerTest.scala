package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteTestData
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.context.AnalysisContext

class ProposedAnalyzerTest extends UnitTest with SharedTestObjects {

  test("proposed") {
    assert(!analyze(Tags.empty))
    assert(analyze(Tags.from("state" -> "proposed")))
  }

  private def analyze(routeTags: Tags): Boolean = {

    val relation = new RouteTestData("01-02", routeTags = routeTags).data.relations(1L)
    val analysisContext = new AnalysisContext()
    val context = RouteAnalysisContext(
      analysisContext,
      relation
    )

    ProposedAnalyzer.analyze(context).proposed
  }
}
