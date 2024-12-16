package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteTestData
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

class ProposedAnalyzerTest extends UnitTest with SharedTestObjects {

  test("proposed") {
    assert(!analyze(Seq.empty))
    assert(analyze(Tags.from("state" -> "proposed")))
  }

  private def analyze(routeTags: Seq[Tag]): Boolean = {

    val relation = new RouteTestData("01-02", routeTags = routeTags).data.relations(1L)
    val context = RouteDetailAnalysisContext(
      relation,
      None
    )

    ProposedAnalyzer.analyze(context).proposed
  }
}
