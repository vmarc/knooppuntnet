package kpn.api.common;

import kpn.api.common.ChangeSetElementRefs;
import kpn.api.common.Country;
import kpn.api.common.RouteType;

import java.util.Optional;

public record ChangeSetNetwork(
  Optional<Country> country,
  RouteType routeType,
  Long networkId,
  Optional<String> networkName,
  ChangeSetElementRefs routeChanges,
  ChangeSetElementRefs nodeChanges,
  Boolean happy,
  Boolean investigate
) {
}

/*
package kpn.api.common

import kpn.api.common.common.ReferencedElements
import kpn.api.custom.Subset

case class ChangeSetNetwork(
  country: Option[Country],
  routeType: RouteType,
  networkId: Long,
  networkName: Option[String],
  routeChanges: ChangeSetElementRefs,
  nodeChanges: ChangeSetElementRefs,
  happy: Boolean,
  investigate: Boolean
) {

  def subsets: Set[Subset] = country.map(c => kpn.api.custom.Subset(c, routeType)).toSet

  def referencedElements: ReferencedElements = {
    ReferencedElements(
      nodeChanges.referencedElementIds,
      routeChanges.referencedElementIds
    )
  }
}

*/
