package kpn.api.common.changes.filter

case class ServerFilterGroup(
  selected: String,
  options: Seq[ServerFilterOption]
)
