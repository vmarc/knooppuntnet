package kpn.server.analyzer.full.network

import kpn.server.analyzer.full.FullAnalysisContext

trait FullNetworkAnalyzer {

  def analyze(context: FullAnalysisContext): FullAnalysisContext

}
