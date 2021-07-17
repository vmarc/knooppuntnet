package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis

trait NodeAspectAnalyzer {
  def analyze(analysis: NodeAnalysis): NodeAnalysis
}
