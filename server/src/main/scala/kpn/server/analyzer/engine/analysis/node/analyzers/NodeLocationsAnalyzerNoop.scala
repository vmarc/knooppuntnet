package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis

class NodeLocationsAnalyzerNoop extends NodeLocationsAnalyzer {
  override def analyze(analysis: NodeAnalysis): NodeAnalysis = analysis
}
