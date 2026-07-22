package kpn.server.monitor.repository

import kpn.api.id.Storable

case class MonitorTileData(
  name: String,
  relationIds: Seq[Long]
) extends Storable
