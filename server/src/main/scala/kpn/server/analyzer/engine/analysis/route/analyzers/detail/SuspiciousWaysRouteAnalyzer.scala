package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.api.common.Fact.RouteSuspiciousWays
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object SuspiciousWaysRouteAnalyzer extends RouteDetailAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new SuspiciousWaysRouteAnalyzer(context).analyze
  }
}

class SuspiciousWaysRouteAnalyzer(context: RouteDetailAnalysisContext) {

  def analyze: RouteDetailAnalysisContext = {
    val suspiciousWayIds = findSuspiciousWayIds
    context.copy(suspiciousWayIds = Some(suspiciousWayIds)).withFact(suspiciousWayIds.nonEmpty, RouteSuspiciousWays)
  }

  private def findSuspiciousWayIds: Seq[Long] = {
    context.relation.wayMembers.filter(_.way.nodes.size <= 1).map(_.way.id)
  }
}
