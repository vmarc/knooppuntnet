package kpn.core.engine.analysis.route.analyzers

import kpn.core.engine.analysis.route.domain.RouteAnalysisContext
import kpn.shared.Fact.RouteSuspiciousWays

object SuspiciousWaysRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new SuspiciousWaysRouteAnalyzer(context).analyze
  }
}

class SuspiciousWaysRouteAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    val suspiciousWayIds = findSuspiciousWayIds
    context.copy(suspiciousWayIds = Some(suspiciousWayIds)).withFact(suspiciousWayIds.nonEmpty, RouteSuspiciousWays)
  }

  private def findSuspiciousWayIds: Seq[Long] = {
    context.loadedRoute.relation.wayMembers.filter(_.way.nodes.size <= 1).map(_.way.id)
  }

}
