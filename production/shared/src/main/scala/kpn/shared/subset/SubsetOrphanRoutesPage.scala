package kpn.shared.subset

import kpn.shared.RouteSummary
import kpn.shared.TimeInfo

case class SubsetOrphanRoutesPage(
  timeInfo: TimeInfo,
  subsetInfo: SubsetInfo,
  rows: Seq[RouteSummary]
)
