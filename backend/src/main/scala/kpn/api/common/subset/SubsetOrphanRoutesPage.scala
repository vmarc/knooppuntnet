package kpn.api.common.subset

import kpn.api.common.OrphanRouteInfo
import kpn.api.common.TimeInfo

case class SubsetOrphanRoutesPage(
  timeInfo: TimeInfo,
  subsetInfo: SubsetInfo,
  routes: Seq[OrphanRouteInfo]
)
