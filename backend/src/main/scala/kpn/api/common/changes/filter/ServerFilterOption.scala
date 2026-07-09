package kpn.api.common.changes.filter

case class ServerFilterOption(
  name: String,
  count: Long,
  selected: Boolean = false
)
