package kpn.server.api.status

import kpn.core.doc.Storable

case class DatabaseSizes(
  file: Long,
  external: Long,
  active: Long
) extends Storable
