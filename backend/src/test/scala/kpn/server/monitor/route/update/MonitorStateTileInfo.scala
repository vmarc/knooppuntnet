package kpn.server.monitor.route.update

import kpn.server.monitor.domain.MonitorStateTile
import kpn.server.monitor.domain.MonitorStateTileDeviation

object MonitorStateTileInfo {
  def from(tile: MonitorStateTile): MonitorStateTileInfo = {
    MonitorStateTileInfo(
      relationId = tile.relationId,
      z = tile.z,
      x = tile.x,
      y = tile.y,
      deviations = tile.deviations,
      matchesLines = tile.matchesLines,
    )
  }
}

case class MonitorStateTileInfo(
  relationId: Long,
  z: Long,
  x: Long,
  y: Long,
  deviations: Seq[MonitorStateTileDeviation],
  matchesLines: Seq[String],
)
