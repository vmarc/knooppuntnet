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

/* TODO migrate

  def subsets: Set[Subset] = country.map(c => kpn.api.custom.Subset(c, routeType)).toSet

  def referencedElements: ReferencedElements = {
    ReferencedElements(
      nodeChanges.referencedElementIds,
      routeChanges.referencedElementIds
    )
  }

*/
