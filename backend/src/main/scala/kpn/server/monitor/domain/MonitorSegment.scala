package kpn.server.monitor.domain

import kpn.api.id.Storable

case class MonitorSegment(
  relationId: Long,
  segmentId: Long,
  coordinates: String
) extends Storable
