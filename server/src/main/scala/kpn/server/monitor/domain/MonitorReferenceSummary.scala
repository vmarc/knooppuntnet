package kpn.server.monitor.domain

import kpn.api.custom.Timestamp

object MonitorReferenceSummary {
  def from(reference: MonitorReference): MonitorReferenceSummary = {
    MonitorReferenceSummary(
      reference.relationId,
      reference.referenceTimestamp,
      reference.referenceDistance,
      reference.referenceFilename,
    )
  }
}

case class MonitorReferenceSummary(
  relationId: Option[Long],
  referenceTimestamp: Timestamp,
  referenceDistance: Long,
  referenceFilename: Option[String],
)
