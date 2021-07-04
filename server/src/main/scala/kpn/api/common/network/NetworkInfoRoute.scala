package kpn.api.common.network

import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.Timestamp

case class NetworkInfoRoute(
  id: Long,
  name: String,
  wayCount: Long,
  length: Long, // length in meter
  role: Option[String],
  relationLastUpdated: Timestamp,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  facts: Seq[Fact],
  proposed: Boolean
)
