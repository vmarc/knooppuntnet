package kpn.api.common.subset;

import kpn.api.common.OrphanRouteInfo;
import kpn.api.common.TimeInfo;
import kpn.api.common.subset.SubsetInfo;

import com.google.common.collect.ImmutableList;

public record SubsetOrphanRoutesPage(
  TimeInfo timeInfo,
  SubsetInfo subsetInfo,
  ImmutableList<OrphanRouteInfo> routes
) {
}

/*
package kpn.api.common.subset

import kpn.api.common.OrphanRouteInfo
import kpn.api.common.TimeInfo

case class SubsetOrphanRoutesPage(
  timeInfo: TimeInfo,
  subsetInfo: SubsetInfo,
  routes: Seq[OrphanRouteInfo]
)

*/
