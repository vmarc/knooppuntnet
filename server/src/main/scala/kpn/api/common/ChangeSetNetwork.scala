package kpn.api.common

import kpn.api.common.common.ReferencedElements
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset

case class ChangeSetNetwork(
  country: Option[Country],
  networkType: NetworkType,
  networkId: Long,
  networkName: String,
  routeChanges: ChangeSetElementRefs,
  nodeChanges: ChangeSetElementRefs,
  happy: Boolean,
  investigate: Boolean
) {

  def subsets: Set[Subset] = country.map(c => kpn.api.custom.Subset(c, networkType)).toSet

  def referencedElements: ReferencedElements = {
    ReferencedElements(
      nodeChanges.referencedElementIds,
      routeChanges.referencedElementIds
    )
  }
}
