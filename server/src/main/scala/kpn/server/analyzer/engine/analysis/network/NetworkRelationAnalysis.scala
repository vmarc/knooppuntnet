package kpn.server.analyzer.engine.analysis.network

import kpn.api.common.data.Node
import kpn.api.custom.Country
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.server.analyzer.engine.context.ElementIds

case class NetworkRelationAnalysis(
  country: Option[Country],
  nodes: Seq[Node],
  routeRelations: Seq[Relation],
  networkRelations: Seq[Relation],
  nodeRefs: Seq[Long],
  routeRefs: Seq[Long],
  networkRefs: Seq[Long],
  lastUpdated: Timestamp,
  elementIds: ElementIds
)
