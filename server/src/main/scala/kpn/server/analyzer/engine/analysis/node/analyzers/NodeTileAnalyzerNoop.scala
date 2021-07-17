package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis

class NodeTileAnalyzerNoop extends NodeTileAnalyzer {
  override def analyze(analysis: NodeAnalysis): NodeAnalysis = analysis
}
