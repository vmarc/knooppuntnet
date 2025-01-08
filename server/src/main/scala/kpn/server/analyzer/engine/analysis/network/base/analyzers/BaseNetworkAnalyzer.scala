package kpn.server.analyzer.engine.analysis.network.base.analyzers

trait BaseNetworkAnalyzer {
  def analyze(context: BaseNetworkAnalysisContext): BaseNetworkAnalysisContext
}
