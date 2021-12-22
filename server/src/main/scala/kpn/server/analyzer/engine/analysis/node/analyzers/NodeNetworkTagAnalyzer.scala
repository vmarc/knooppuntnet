package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis

object NodeNetworkTagAnalyzer extends NodeAspectAnalyzer {
  def analyze(analysis: NodeAnalysis): NodeAnalysis = {
    new NodeNetworkTagAnalyzer(analysis).analyze
  }
}

class NodeNetworkTagAnalyzer(analysis: NodeAnalysis) {

  def analyze: NodeAnalysis = {
    if (!analysis.node.tags.has("network:type", "node_network")) {
      analysis.copy(abort = true)
    }
    else {
      analysis
    }
  }
}
