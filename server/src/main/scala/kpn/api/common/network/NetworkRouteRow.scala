package kpn.api.common.network

import kpn.api.custom.Timestamp

case class NetworkRouteRow(
  id: Long,
  name: String,
  length: Int,
  role: Option[String],
  investigate: Boolean,
  accessible: Boolean,
  roleConnection: Boolean,
  relationLastUpdated: Timestamp
)
