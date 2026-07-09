package kpn.server.analyzer.engine.analysis.node.base.analyzers

trait BaseNodeAnalyzer {
  def analyze(context: BaseNodeAnalysisContext): BaseNodeAnalysisContext
}
