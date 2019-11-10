package kpn.api.common.network

import kpn.api.custom.Fact
import kpn.api.custom.Timestamp

case class NetworkRouteInfo(
  id: Long,
  name: String,
  wayCount: Int,
  length: Int, // length in meter
  role: Option[String],
  relationLastUpdated: Timestamp,
  lastUpdated: Timestamp,
  facts: Seq[Fact]
)
