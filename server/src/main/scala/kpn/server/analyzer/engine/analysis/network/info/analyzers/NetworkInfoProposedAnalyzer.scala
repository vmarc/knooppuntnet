package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext

object NetworkInfoProposedAnalyzer extends NetworkInfoAnalyzer {
  override def analyze(context: NetworkInfoAnalysisContext): NetworkInfoAnalysisContext = {
    new NetworkInfoProposedAnalyzer(context).analyze()
  }
}

class NetworkInfoProposedAnalyzer(context: NetworkInfoAnalysisContext) {
  def analyze(): NetworkInfoAnalysisContext = {
    context.copy(
      proposed = context.networkDoc.tags.has("state", "proposed")
    )
  }
}
