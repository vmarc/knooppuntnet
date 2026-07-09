package kpn.server.analyzer.engine.analysis.node.main.analyzers

trait NodeAnalyzer {
  def analyze(context: NodeAnalysisContext): NodeAnalysisContext
}
