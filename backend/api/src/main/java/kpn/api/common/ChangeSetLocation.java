package kpn.api.common;

import kpn.api.common.ChangeSetElementRefs;
import kpn.api.common.RouteType;

public record ChangeSetLocation(
  RouteType routeType,
  String locationName,
  ChangeSetElementRefs routeChanges,
  ChangeSetElementRefs nodeChanges,
  Boolean happy,
  Boolean investigate
) {
}

/*
package kpn.api.common

case class ChangeSetLocation(
  routeType: RouteType,
  locationName: String,
  routeChanges: ChangeSetElementRefs,
  nodeChanges: ChangeSetElementRefs,
  happy: Boolean,
  investigate: Boolean
)

*/
