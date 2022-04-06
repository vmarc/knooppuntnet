package kpn.server.api.monitor.domain

import kpn.api.base.WithStringId

case class MonitorRoute(
  _id: String,
  groupName: String,
  name: String,
  description: String,
  routeId: Long
) extends WithStringId
