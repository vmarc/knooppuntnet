package kpn.api.common

import kpn.api.custom.Timestamp

case class OrphanRouteInfo(
  id: Long,
  name: String,
  meters: Long,
  lastSurvey: Option[String],
  lastUpdated: Timestamp,
  facts: Seq[Fact],
  investigate: Boolean
)
