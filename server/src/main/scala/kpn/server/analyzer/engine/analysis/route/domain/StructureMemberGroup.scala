package kpn.server.analyzer.engine.analysis.route.domain

import kpn.api.common.data.Member

case class StructureMemberGroup(
  role: Option[String],
  members: Seq[Member]
)
