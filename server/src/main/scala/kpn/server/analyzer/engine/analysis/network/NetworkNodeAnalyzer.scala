package kpn.server.analyzer.engine.analysis.network

import kpn.api.custom.NetworkType
import kpn.core.data.Data
import kpn.server.analyzer.engine.analysis.node.NodeAnalysis

trait NetworkNodeAnalyzer {
  def analyze(networkType: NetworkType, data: Data): Map[Long, NodeAnalysis]
}
