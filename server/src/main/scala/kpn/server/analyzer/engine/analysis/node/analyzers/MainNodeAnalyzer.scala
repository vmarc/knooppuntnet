package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.data.Node
import kpn.server.analyzer.engine.analysis.node.NodeAnalysis

trait MainNodeAnalyzer {
  def analyze(node: Node): NodeAnalysis
}
