package kpn.server.analyzer.engine.changes.diff

import kpn.shared.Country
import kpn.shared.NetworkType
import kpn.shared.Subset
import kpn.shared.common.Ref
import kpn.shared.common.ReferencedElements
import kpn.shared.diff.IdDiffs
import kpn.shared.diff.NetworkDataUpdate

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
