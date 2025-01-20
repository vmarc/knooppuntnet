package kpn.server.analyzer.engine.analysis.network.main.analyzers

object NetworkProposedAnalyzer extends NetworkAnalyzer {
  override def analyze(context: NetworkAnalysisContext): NetworkAnalysisContext = {
    new NetworkProposedAnalyzer(context).analyze()
  }
}

class NetworkProposedAnalyzer(context: NetworkAnalysisContext) {
  def analyze(): NetworkAnalysisContext = {
    context.copy(
      _proposed = Some(context.network.hasTag("state", "proposed"))
    )
  }
}
