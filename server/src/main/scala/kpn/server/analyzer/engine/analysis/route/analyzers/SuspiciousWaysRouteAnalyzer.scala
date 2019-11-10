package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact.RouteSuspiciousWays
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

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
