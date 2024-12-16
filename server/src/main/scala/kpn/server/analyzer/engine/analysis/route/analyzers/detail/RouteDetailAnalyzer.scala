package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

trait RouteDetailAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext
}
