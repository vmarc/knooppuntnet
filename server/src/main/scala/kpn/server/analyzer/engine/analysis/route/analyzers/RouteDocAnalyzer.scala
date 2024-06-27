package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.server.analyzer.engine.analysis.route.domain.RouteDocAnalysisContext

trait RouteDocAnalyzer {
  def analyze(context: RouteDocAnalysisContext): RouteDocAnalysisContext
}
