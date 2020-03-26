package kpn.server.api.status

case class DatabaseSizes(
  file: Long,
  external: Long,
  active: Long
)
