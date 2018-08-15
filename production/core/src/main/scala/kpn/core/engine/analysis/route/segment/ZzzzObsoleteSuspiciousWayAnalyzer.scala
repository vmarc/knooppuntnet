package kpn.core.engine.analysis.route.segment

import kpn.shared.data.WayMember

class ZzzzObsoleteSuspiciousWayAnalyzer(wayMembers: Seq[WayMember]) {

  def suspiciousWayIds: Seq[Long] = {
    wayMembers.filter(_.way.nodes.size <= 1).map(_.way.id)
  }
}
