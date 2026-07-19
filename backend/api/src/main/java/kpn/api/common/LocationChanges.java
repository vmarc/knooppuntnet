package kpn.api.common;

import kpn.api.common.ChangeSetElementRefs;
import kpn.api.common.RouteType;

import com.google.common.collect.ImmutableList;

public record LocationChanges(
  RouteType routeType,
  ImmutableList<String> locationNames,
  ChangeSetElementRefs routeChanges,
  ChangeSetElementRefs nodeChanges,
  Boolean happy,
  Boolean investigate
) {
}

/*
package kpn.api.common

case class LocationChanges(
  routeType: RouteType,
  locationNames: Seq[String],
  routeChanges: ChangeSetElementRefs,
  nodeChanges: ChangeSetElementRefs,
  happy: Boolean,
  investigate: Boolean
)

*/
