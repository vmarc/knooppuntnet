package kpn.server.analyzer.engine.monitor.structure

import kpn.api.common.data.Member

case class MonitorRouteMemberGroup(
  role: Option[String],
  members: Seq[Member]
)
