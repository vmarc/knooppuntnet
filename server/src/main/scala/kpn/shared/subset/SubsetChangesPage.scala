package kpn.shared.subset

import kpn.shared.ChangeSetSummaryInfo
import kpn.shared.changes.filter.ChangesFilter

case class SubsetChangesPage(
  subsetInfo: SubsetInfo,
  filter: ChangesFilter,
  changes: Seq[ChangeSetSummaryInfo],
  changeCount: Int
)
