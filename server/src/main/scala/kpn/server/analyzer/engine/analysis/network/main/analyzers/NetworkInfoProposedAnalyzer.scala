package kpn.server.analyzer.engine.analysis.network.main.analyzers

object NetworkInfoProposedAnalyzer extends NetworkAnalyzer {
  override def analyze(context: NetworkAnalysisContext): NetworkAnalysisContext = {
    new NetworkInfoProposedAnalyzer(context).analyze()
  }
}

class NetworkInfoProposedAnalyzer(context: NetworkAnalysisContext) {
  def analyze(): NetworkAnalysisContext = {
    context.copy(
      proposed = context.network.hasTag("state", "proposed")
    )
  }
}
