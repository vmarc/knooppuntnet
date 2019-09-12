package kpn.shared.network

import kpn.shared.Timestamp

case class NetworkRouteRow(
  id: Long,
  name: String,
  length: Int,
  role: Option[String],
  investigate: Boolean,
  accessible: Boolean,
  roleConnection: Boolean,
  tagged: Boolean,
  relationLastUpdated: Timestamp
)
