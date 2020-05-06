package kpn.api.common.network

import kpn.api.common.common.Day
import kpn.api.custom.Timestamp

case class NetworkRouteRow(
  id: Long,
  name: String,
  length: Long,
  role: Option[String],
  investigate: Boolean,
  accessible: Boolean,
  roleConnection: Boolean,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day]
)
