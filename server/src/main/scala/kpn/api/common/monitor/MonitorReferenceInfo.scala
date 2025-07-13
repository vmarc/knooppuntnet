package kpn.api.common.monitor

import kpn.api.common.Bounds
import kpn.api.custom.Timestamp

case class MonitorReferenceInfo(
  created: Timestamp,
  user: String,
  referenceBounds: Bounds,
  referenceDistance: Long,
  referenceType: MonitorReferenceType,
  referenceTimestamp: Timestamp,
  referenceSegmentCount: Long,
  referenceFilename: Option[String]
)
