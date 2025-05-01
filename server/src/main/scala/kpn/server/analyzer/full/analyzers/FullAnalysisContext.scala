package kpn.server.analyzer.full.analyzers

import kpn.api.custom.Timestamp
import kpn.server.analyzer.engine.changes.ChangeSetContext

case class FullAnalysisContext(
  timestamp: Timestamp,
  initialAnalysisChangeSetContext: Option[ChangeSetContext] = None,
  nodeIds: Seq[Long] = Seq.empty,
  obsoleteNodeIds: Seq[Long] = Seq.empty,
  networkIds: Seq[Long] = Seq.empty,
  obsoleteNetworkIds: Seq[Long] = Seq.empty,
)
