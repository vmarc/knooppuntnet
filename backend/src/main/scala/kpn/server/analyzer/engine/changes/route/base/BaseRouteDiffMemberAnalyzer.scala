package kpn.server.analyzer.engine.changes.route.base

import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class BaseRouteDiffMemberAnalyzer {

  def memberOrderChanged(before: BaseRouteAnalysisContext, after: BaseRouteAnalysisContext): Boolean = {
    val beforeMembers = before.routeMembers
    val afterMembers = after.routeMembers

    if (beforeMembers.sizeIs == afterMembers.size) {
      val beforeMemberIds = beforeMembers.map(member => member.memberType -> member.id)
      val afterMemberIds = afterMembers.map(member => member.memberType -> member.id)
      beforeMemberIds != afterMemberIds && beforeMemberIds.toSet == afterMemberIds.toSet
    }
    else {
      false
    }
  }
}
