package kpn.server.analyzer.engine.analysis.network

import kpn.api.common.data.Node
import kpn.api.custom.{Country, Relation, ScopedNetworkType, Timestamp}
import kpn.server.analyzer.engine.changes.changes.ElementIds

case class NetworkRelationAnalysis(
  country: Option[Country],
  scopedNetworkType: ScopedNetworkType,
  nodes: Seq[Node],
  routeRelations: Seq[Relation],
  networkRelations: Seq[Relation],
  nodeRefs: Seq[Long],
  routeRefs: Seq[Long],
  networkRefs: Seq[Long],
  lastUpdated: Timestamp,
  elementIds: ElementIds
)
