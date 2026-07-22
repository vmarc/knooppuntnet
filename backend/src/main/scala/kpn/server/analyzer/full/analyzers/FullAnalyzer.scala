package kpn.server.analyzer.full.analyzers

trait FullAnalyzer {
  def analyze(context: FullAnalysisContext): FullAnalysisContext
}
