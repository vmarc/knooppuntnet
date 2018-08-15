package kpn.core.engine.analysis

import kpn.core.changes.ElementIds
import kpn.shared.Country
import kpn.shared.Timestamp
import kpn.shared.data.Node
import kpn.shared.data.Relation

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
) {

  def isNetworkCollection: Boolean = networkRelations.nonEmpty

}
