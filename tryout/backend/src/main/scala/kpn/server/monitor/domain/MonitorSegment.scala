package kpn.server.monitor.domain

import kpn.core.doc.Storable

case class MonitorSegment(
  relationId: Long,
  segmentId: Long,
  coordinates: String
) extends Storable
