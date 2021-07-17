package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.data.Node
import kpn.server.analyzer.engine.analysis.node.OldNodeAnalysis

trait OldMainNodeAnalyzer {
  def analyze(node: Node): OldNodeAnalysis
}
