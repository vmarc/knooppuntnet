package kpn.shared.network

import kpn.shared.Fact
import kpn.shared.Timestamp

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
