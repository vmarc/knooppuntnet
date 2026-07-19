package kpn.api.common.monitor

import kpn.api.custom.Timestamp

case class MonitorRouteProperties(
  groupName: String,
  name: String,
  description: String,
  comment: Option[String],
  relationId: Option[Long],
  referenceType: MonitorReferenceType,
  referenceTimestamp: Option[Timestamp],
  referenceFilename: Option[String],
  referenceFileChanged: Boolean,
)
