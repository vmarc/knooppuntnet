package kpn.api.common

import kpn.api.common.changes.filter.ChangesFilter

case class ChangesPage(
  filter: ChangesFilter,
  changes: Seq[ChangeSetSummaryInfo],
  changeCount: Long
)
