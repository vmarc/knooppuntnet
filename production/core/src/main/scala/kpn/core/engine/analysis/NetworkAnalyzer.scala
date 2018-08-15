package kpn.core.engine.analysis

import kpn.core.analysis.Network
import kpn.core.data.Data

trait NetworkAnalyzer {
  def analyze(networkRelationAnalysis: NetworkRelationAnalysis, data: Data, networkId: Long): Network
}
