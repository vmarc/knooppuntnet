package kpn.api.common.monitor

import kpn.api.common.Bounds

case class MonitorRouteDeviation(
  id: Long,
  meters: Long,
  distance: Long,
  bounds: Bounds,
  lines: Seq[String]
) {
  def sameAs(other: MonitorRouteDeviation): Boolean = {
    lines.toSet == other.lines.toSet
  }
}
