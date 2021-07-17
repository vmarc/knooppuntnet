package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis

class NodeCountryAnalyzerNoop extends NodeCountryAnalyzer {
  def analyze(analysis: NodeAnalysis): NodeAnalysis = analysis
}
