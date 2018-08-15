package kpn.core.engine.changes.network.update

import kpn.core.changes.ElementIds
import kpn.core.engine.analysis.NetworkRelationAnalysis
import kpn.core.engine.changes.data.AnalysisData

class NetworkUpdateCollectionProcessorImpl(
  analysisData: AnalysisData
) extends NetworkUpdateCollectionProcessor {

  override def process(networkId: Long, networkRelationAnalysisAfter: NetworkRelationAnalysis): Unit = {

    if (analysisData.networks.watched.contains(networkId)) {
      analysisData.networks.watched.delete(networkId)
    }

    analysisData.networks.ignored.add(networkId, ElementIds())
  }
}
