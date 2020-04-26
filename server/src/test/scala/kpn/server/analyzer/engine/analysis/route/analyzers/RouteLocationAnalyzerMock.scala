package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.RouteLocationAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

class RouteLocationAnalyzerMock extends RouteLocationAnalyzer {

  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    context.copy(locationAnalysis = Some(RouteLocationAnalysis(None, Seq.empty, Seq.empty)))
  }
}
