package kpn.server.analyzer.engine.changes.diff

import kpn.api.common.common.Ref
import kpn.api.common.common.ReferencedElements
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset

/*
 * Describes the differences in network definition between two points in time ('before' and 'after').
 */
case class NetworkDiff(
  country: Option[Country],
  networkType: NetworkType,
  networkId: Long,
  networkName: String,
  networkDataUpdate: Option[NetworkDataUpdate],
  networkNodes: NetworkNodeDiffs,
  routes: RouteDiffs,
  nodes: IdDiffs,
  ways: IdDiffs,
  relations: IdDiffs,
  happy: Boolean,
  investigate: Boolean
) {

  def subsets: Seq[Subset] = country.toSeq.flatMap(country => Subset.of(country, networkType))

  def toRef: Ref = Ref(networkId, networkName)

  def referencedElements: ReferencedElements = {
    ReferencedElements.merge(
      networkNodes.referencedElements,
      routes.referencedElements,
      ReferencedElements(nodeIds = nodes.ids.toSet)
    )
  }
}
