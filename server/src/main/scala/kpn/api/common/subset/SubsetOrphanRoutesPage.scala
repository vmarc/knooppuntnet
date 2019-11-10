package kpn.api.common.subset

import kpn.api.common.RouteSummary
import kpn.api.common.TimeInfo

case class SubsetOrphanRoutesPage(
  timeInfo: TimeInfo,
  subsetInfo: SubsetInfo,
  rows: Seq[RouteSummary]
)
