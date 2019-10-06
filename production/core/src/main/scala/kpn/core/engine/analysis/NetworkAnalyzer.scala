package kpn.core.engine.analysis

import kpn.core.analysis.Network
import kpn.core.data.Data
import kpn.shared.NetworkType

trait NetworkAnalyzer {
  def analyze(networkRelationAnalysis: NetworkRelationAnalysis, data: Data, networkType: NetworkType, networkId: Long): Network
}
