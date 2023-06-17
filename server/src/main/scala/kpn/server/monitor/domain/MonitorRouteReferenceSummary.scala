package kpn.server.monitor.domain

import kpn.api.custom.Timestamp

object MonitorRouteReferenceSummary {
  def from(reference: MonitorRouteReference): MonitorRouteReferenceSummary = {
    MonitorRouteReferenceSummary(
      reference.relationId,
      reference.referenceTimestamp,
      reference.referenceDistance,
      reference.referenceFilename,
    )
  }
}

case class MonitorRouteReferenceSummary(
  relationId: Option[Long],
  referenceTimestamp: Timestamp,
  referenceDistance: Long,
  referenceFilename: Option[String],
)
