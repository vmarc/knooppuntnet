package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.data.raw.RawMember
import kpn.core.analysis.BreakPoint

case class Tentacle(tentacleId: Int, wayMembers: Seq[RawMember], nextTentacleId: Int, breakPoint: Option[BreakPoint])
