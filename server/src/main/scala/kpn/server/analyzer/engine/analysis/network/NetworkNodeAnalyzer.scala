package kpn.server.analyzer.engine.analysis.network

import kpn.api.custom.ScopedNetworkType
import kpn.core.analysis.NetworkNode
import kpn.core.data.Data

trait NetworkNodeAnalyzer {
  def analyze(scopedNetworkType: ScopedNetworkType, data: Data): Map[Long, NetworkNode]
}
