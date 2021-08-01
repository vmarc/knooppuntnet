package kpn.api.common

import kpn.api.custom.Timestamp

case class OrphanNodeInfo(
  id: Long,
  name: String,
  longName: String,
  lastUpdated: Timestamp,
  proposed: Boolean,
  lastSurvey: String,
  factCount: Long,
)
