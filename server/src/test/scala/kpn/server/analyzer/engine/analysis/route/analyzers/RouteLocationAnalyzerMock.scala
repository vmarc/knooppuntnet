package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.RouteLocationAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

class RouteLocationAnalyzerMock extends RouteLocationAnalyzer {

  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    context.copy(locationAnalysis = Some(RouteLocationAnalysis(None, Seq.empty, Seq.empty)))
  }
}
