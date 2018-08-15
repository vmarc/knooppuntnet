package kpn.shared

import kpn.shared.changes.filter.ChangesFilter

case class ChangesPage(
  filter: ChangesFilter,
  changes: Seq[ChangeSetSummaryInfo],
  totalCount: Int
)
