package kpn.api.common;

import kpn.api.common.ChangeSetElementRefs;
import kpn.api.common.LocationInfo;
import kpn.api.common.RouteType;

import com.google.common.collect.ImmutableList;

public record LocationChangesInfo(
  RouteType routeType,
  ImmutableList<LocationInfo> locationInfos,
  ChangeSetElementRefs routeChanges,
  ChangeSetElementRefs nodeChanges,
  Boolean happy,
  Boolean investigate
) {
}

/*
package kpn.api.common

case class LocationChangesInfo(
  routeType: RouteType,
  locationInfos: Seq[LocationInfo],
  routeChanges: ChangeSetElementRefs,
  nodeChanges: ChangeSetElementRefs,
  happy: Boolean,
  investigate: Boolean
)

*/
