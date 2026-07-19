package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.RouteLocationAnalysis

class BaseRouteLocationAnalyzerMock extends BaseRouteLocationAnalyzer {

  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    context.copy(_locationAnalysis = Some(RouteLocationAnalysis(None, Seq.empty, Seq.empty)))
  }
}
