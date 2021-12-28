package kpn.api.common

import kpn.api.common.changes.filter.ChangesFilterOption

case class ChangesPage(
  filterOptions: Seq[ChangesFilterOption] = Seq.empty,
  changes: Seq[ChangeSetSummaryInfo] = Seq.empty,
  changeCount: Long = 0
)
