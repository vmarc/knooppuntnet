package kpn.server.analyzer.engine.analysis.network.main.analyzers

trait NetworkAnalyzer {
  def analyze(context: NetworkAnalysisContext): NetworkAnalysisContext
}
