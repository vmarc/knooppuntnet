package kpn.core.engine.analysis.route.analyzers

import kpn.core.engine.analysis.route.domain.RouteAnalysisContext

trait RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext
}
