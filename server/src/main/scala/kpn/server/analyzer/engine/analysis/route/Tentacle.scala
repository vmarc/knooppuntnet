package kpn.server.analyzer.engine.analysis.route

import kpn.core.analysis.BreakPoint
import kpn.shared.data.raw.RawMember

case class Tentacle(tentacleId: Int, wayMembers: Seq[RawMember], nextTentacleId: Int, breakPoint: Option[BreakPoint])
