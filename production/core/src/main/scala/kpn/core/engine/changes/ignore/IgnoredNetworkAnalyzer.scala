package kpn.core.engine.changes.ignore

import kpn.core.engine.analysis.NetworkRelationAnalysis
import kpn.core.load.data.LoadedNetwork
import kpn.shared.Fact

trait IgnoredNetworkAnalyzer {
  def analyze(networkRelationAnalysis: NetworkRelationAnalysis, network: LoadedNetwork): Seq[Fact]
}
