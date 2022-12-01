package kpn.api.common.monitor

import kpn.api.custom.Day

case class MonitorRouteProperties(
  name: String,
  description: String,
  groupName: String,
  relationId: Option[Long],
  referenceType: Option[String],
  referenceDay: Option[Day],
  referenceFilename: Option[String],
  referenceFileChanged: Boolean,
  comment: Option[String]
)
