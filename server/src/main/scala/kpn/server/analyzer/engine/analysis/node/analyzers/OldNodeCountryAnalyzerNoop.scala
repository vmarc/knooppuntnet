package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis

class OldNodeCountryAnalyzerNoop extends OldNodeCountryAnalyzer {
  def analyze(analysis: NodeAnalysis): NodeAnalysis = analysis
}
