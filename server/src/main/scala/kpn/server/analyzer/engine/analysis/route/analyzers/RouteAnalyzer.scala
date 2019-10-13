package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

trait RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext
}
