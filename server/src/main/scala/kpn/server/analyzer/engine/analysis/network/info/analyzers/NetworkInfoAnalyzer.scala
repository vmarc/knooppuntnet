package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext

trait NetworkInfoAnalyzer {

  def analyze(context: NetworkInfoAnalysisContext): NetworkInfoAnalysisContext

}
