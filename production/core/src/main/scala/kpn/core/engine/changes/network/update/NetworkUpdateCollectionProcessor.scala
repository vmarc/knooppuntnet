package kpn.core.engine.changes.network.update

import kpn.core.engine.analysis.NetworkRelationAnalysis

trait NetworkUpdateCollectionProcessor {
  def process(networkId: Long, networkRelationAnalysisAfter: NetworkRelationAnalysis): Unit
}
