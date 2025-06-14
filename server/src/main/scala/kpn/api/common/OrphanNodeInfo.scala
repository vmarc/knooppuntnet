package kpn.api.common

import kpn.api.custom.Timestamp

case class OrphanNodeInfo(
  id: Long,
  name: String,
  longName: Option[String],
  lastUpdated: Timestamp,
  proposed: Boolean,
  lastSurvey: Option[String],
  factCount: Long,
)
