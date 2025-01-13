package kpn.server.analyzer.full.analyzers

import kpn.api.custom.Timestamp

case class FullAnalysisContext(
  timestamp: Timestamp,
  initialize: Boolean = true,
  nodeIds: Seq[Long] = Seq.empty,
  obsoleteNodeIds: Seq[Long] = Seq.empty,
  networkIds: Seq[Long] = Seq.empty,
  obsoleteNetworkIds: Seq[Long] = Seq.empty
)
