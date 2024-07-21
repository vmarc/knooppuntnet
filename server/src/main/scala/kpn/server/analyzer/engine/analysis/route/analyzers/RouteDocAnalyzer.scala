package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

trait RouteDocAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext
}
