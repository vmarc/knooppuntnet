package kpn.api.common.subset

import kpn.api.common.ChangeSetSummaryInfo
import kpn.api.common.changes.filter.ChangesFilter

case class SubsetChangesPage(
  subsetInfo: SubsetInfo,
  filter: ChangesFilter,
  changes: Seq[ChangeSetSummaryInfo],
  changeCount: Long
)
