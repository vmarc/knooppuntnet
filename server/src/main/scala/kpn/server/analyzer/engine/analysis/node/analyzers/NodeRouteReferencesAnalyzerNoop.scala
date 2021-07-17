package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis

class NodeRouteReferencesAnalyzerNoop extends NodeRouteReferencesAnalyzer {
  override def analyze(analysis: NodeAnalysis): NodeAnalysis = analysis
}
