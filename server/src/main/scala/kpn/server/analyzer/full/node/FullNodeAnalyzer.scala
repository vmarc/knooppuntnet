package kpn.server.analyzer.full.node

import kpn.server.analyzer.full.FullAnalysisContext

trait FullNodeAnalyzer {

  def analyze(context: FullAnalysisContext): FullAnalysisContext

}
