package kpn.core.engine.changes.network.create

import kpn.core.engine.analysis.NetworkRelationAnalyzer
import kpn.core.load.data.LoadedNetwork
import kpn.core.repository.AnalysisRepository
import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.AnalysisData
import kpn.core.tools.db.IgnoredNetworkInfoBuilder
import kpn.shared.Fact

class NetworkCreateIgnoredProcessorImpl(
  analysisData: AnalysisData,
  analysisRepository: AnalysisRepository,
  networkRelationAnalyzer: NetworkRelationAnalyzer
) extends NetworkCreateIgnoredProcessor {

  def process(context: ChangeSetContext, loadedNetwork: LoadedNetwork, ignoreReasons: Seq[Fact]): Unit = {
    val networkRelationAnalysis = networkRelationAnalyzer.analyze(loadedNetwork.relation)
    analysisData.networks.ignored.add(loadedNetwork.networkId, networkRelationAnalysis.elementIds)
    val networkInfo = IgnoredNetworkInfoBuilder.build(networkRelationAnalysis, loadedNetwork, ignoreReasons)
    analysisRepository.saveIgnoredNetwork(networkInfo)
  }
}
