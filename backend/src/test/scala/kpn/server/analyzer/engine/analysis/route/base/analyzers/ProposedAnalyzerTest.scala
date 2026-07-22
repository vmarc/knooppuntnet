package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteTestData

class ProposedAnalyzerTest extends UnitTest {

  test("proposed") {
    assert(!analyze(Seq.empty))
    assert(analyze(Tags.from("state" -> "proposed")))
  }

  private def analyze(routeTags: Seq[Tag]): Boolean = {

    val relation = new RouteTestData("01-02", routeTags = routeTags).data.relations(1L)
    val context = BaseRouteAnalysisContext(
      relation,
      None
    )

    BaseRouteProposedAnalyzer.analyze(context).proposed
  }
}
