package kpn.core.tools.analysis

import kpn.api.custom.Timestamp

case class AnalysisStartContext(
  timestamp: Timestamp = Timestamp.analysisStart,
  //  initialize: Boolean = true,
  //  nodeIds: Seq[Long] = Seq.empty,
  //  obsoleteNodeIds: Seq[Long] = Seq.empty,
  networkIds: Seq[Long] = Seq.empty,
  //  obsoleteNetworkIds: Seq[Long] = Seq.empty
)

