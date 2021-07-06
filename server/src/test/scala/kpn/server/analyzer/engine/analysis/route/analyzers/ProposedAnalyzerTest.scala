package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.SharedTestObjects
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteTestData
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.data.LoadedRoute

class ProposedAnalyzerTest extends UnitTest with SharedTestObjects {

  test("proposed") {
    assert(!analyze(Tags.empty))
    assert(analyze(Tags.from("state" -> "proposed")))
  }

  private def analyze(routeTags: Tags): Boolean = {

    val data = new RouteTestData("01-02", routeTags = routeTags).data

    val loadedRoute = LoadedRoute(
      country = None,
      scopedNetworkType = ScopedNetworkType.rwn,
      data,
      data.relations(1L)
    )

    val analysisContext = new AnalysisContext(oldTagging = false)

    val context = RouteAnalysisContext(
      analysisContext = analysisContext,
      loadedRoute = loadedRoute,
      orphan = false,
      Map.empty
    )

    ProposedAnalyzer.analyze(context).proposed
  }
}
