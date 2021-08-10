package kpn.server.analyzer.engine.analysis.node

import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis

trait NodeAnalyzer {
  def analyze(analysis: NodeAnalysis): Option[NodeAnalysis]
}
