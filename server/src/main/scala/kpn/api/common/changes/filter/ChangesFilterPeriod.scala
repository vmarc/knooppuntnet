package kpn.api.common.changes.filter

case class ChangesFilterPeriod(
  name: String,
  totalCount: Long,
  impactedCount: Long,
  current: Boolean = false,
  selected: Boolean = false,
  periods: Seq[ChangesFilterPeriod] = Seq.empty
)
