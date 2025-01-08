package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis

class OldNodeTileAnalyzerNoop extends OldNodeTileAnalyzer {
  override def analyze(analysis: NodeAnalysis): NodeAnalysis = analysis
}
