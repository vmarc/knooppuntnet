package kpn.api.common.subset

import kpn.api.common.ChangeSetSummaryInfo
import kpn.api.common.changes.filter.ChangesFilterOption

case class SubsetChangesPage(
  subsetInfo: SubsetInfo,
  filterOptions: Seq[ChangesFilterOption],
  changes: Seq[ChangeSetSummaryInfo],
  changeCount: Long
)
