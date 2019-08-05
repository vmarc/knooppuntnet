package kpn.shared.network

import kpn.shared.Timestamp

case class NetworkRouteRow(
  id: Long,
  name: String,
  accessible: Boolean,
  roleConnection: Boolean,
  broken: Boolean,
  lastUpdated: Timestamp
)
