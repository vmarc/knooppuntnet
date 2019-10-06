package kpn.core.engine.analysis

import kpn.core.analysis.Network
import kpn.core.load.data.LoadedNetwork

trait NetworkAnalyzer {
  def analyze(networkRelationAnalysis: NetworkRelationAnalysis, loadedNetwork: LoadedNetwork): Network
}
