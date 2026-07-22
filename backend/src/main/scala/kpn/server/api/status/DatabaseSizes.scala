package kpn.server.api.status

import kpn.api.id.Storable

case class DatabaseSizes(
  file: Long,
  external: Long,
  active: Long
) extends Storable
