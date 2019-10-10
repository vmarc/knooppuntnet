package kpn.shared

import kpn.shared.common.ReferencedElements
import kpn.shared.common.ToStringBuilder

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

  def subsets: Set[Subset] = country.map(c => Subset(c, networkType)).toSet

  def referencedElements: ReferencedElements = {
    ReferencedElements(
      nodeChanges.referencedElementIds,
      routeChanges.referencedElementIds
    )
  }

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("country", country).
    field("networkType", networkType).
    field("networkId", networkId).
    field("networkName", networkName).
    field("routeChanges", routeChanges).
    field("nodeChanges", nodeChanges).
    field("happy", happy).
    field("investigate", investigate).
    build
}
