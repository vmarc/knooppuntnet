package kpn.api.common.changes.filter

case class ChangesFilterOption(
  level: String,
  name: String,
  year: Long,
  month: Option[Long],
  day: Option[Long],
  totalCount: Long,
  impactedCount: Long,
  current: Boolean = false
)
