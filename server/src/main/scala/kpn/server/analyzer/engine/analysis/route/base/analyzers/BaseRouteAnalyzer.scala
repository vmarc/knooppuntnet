package kpn.server.analyzer.engine.analysis.route.base.analyzers

trait BaseRouteAnalyzer {
  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext
}
