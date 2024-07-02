package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact.RouteWithoutWays
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object WithoutWaysRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new WithoutWaysRouteAnalyzer(context).analyze
  }
}

class WithoutWaysRouteAnalyzer(context: RouteDetailAnalysisContext) {

  def analyze: RouteDetailAnalysisContext = {
    if (hasWays || isSuperRoute || hasRelationMembers) {
      context
    }
    else {
      context.withFact(RouteWithoutWays).withOldFact(RouteWithoutWays)
    }
  }

  private def isSuperRoute: Boolean = {
    context.relation.hasTag("type", "superroute")
  }

  private def hasRelationMembers: Boolean = {
    context.relation.relationMembers.nonEmpty
  }

  private def hasWays: Boolean = {
    context.relation.members.exists(_.isWay)
  }
}
