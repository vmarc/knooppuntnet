package kpn.api.common

import kpn.api.custom.Timestamp

case class OrphanNodeInfo(
  id: Long,
  name: String,
  longName: String,
  lastUpdated: Timestamp,
  lastSurvey: String,
  factCount: Long,
)
