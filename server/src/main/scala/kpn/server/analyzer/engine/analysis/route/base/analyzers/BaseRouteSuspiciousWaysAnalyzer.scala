package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact.RouteSuspiciousWays

object BaseRouteSuspiciousWaysAnalyzer extends BaseRouteAnalyzer {
  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    new BaseRouteSuspiciousWaysAnalyzer(context).analyze
  }
}

class BaseRouteSuspiciousWaysAnalyzer(context: BaseRouteAnalysisContext) {

  def analyze: BaseRouteAnalysisContext = {
    val suspiciousWayIds = findSuspiciousWayIds
    context.copy(suspiciousWayIds = Some(suspiciousWayIds)).withFact(suspiciousWayIds.nonEmpty, RouteSuspiciousWays)
  }

  private def findSuspiciousWayIds: Seq[Long] = {
    context.relation.wayMembers.filter(_.way.nodes.size <= 1).map(_.way.id)
  }
}
