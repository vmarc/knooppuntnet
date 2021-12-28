package kpn.api.common

import kpn.api.common.changes.filter.ChangesFilterOption

case class ChangesPage(
  filterOptions: Seq[ChangesFilterOption],
  changes: Seq[ChangeSetSummaryInfo],
  changeCount: Long
)
