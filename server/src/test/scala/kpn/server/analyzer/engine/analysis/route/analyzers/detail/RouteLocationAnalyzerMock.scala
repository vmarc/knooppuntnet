package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.api.common.RouteLocationAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

class RouteLocationAnalyzerMock extends RouteLocationAnalyzer {

  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    context.copy(_locationAnalysis = Some(RouteLocationAnalysis(None, Seq.empty, Seq.empty)))
  }
}
