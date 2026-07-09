package kpn.server.monitor.repository

import kpn.core.doc.Storable

case class MonitorTileData(
  name: String,
  relationIds: Seq[Long]
) extends Storable
