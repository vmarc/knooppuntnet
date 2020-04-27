package kpn.server.analyzer.engine.analysis.network

import kpn.api.custom.NetworkType
import kpn.core.analysis.NetworkNode
import kpn.core.data.Data

trait NetworkNodeAnalyzer {
  def analyze(networkType: NetworkType, data: Data): Map[Long, NetworkNode]
}
