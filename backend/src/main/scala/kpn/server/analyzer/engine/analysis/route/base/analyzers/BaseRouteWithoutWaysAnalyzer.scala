package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact.RouteWithoutWays

object BaseRouteWithoutWaysAnalyzer extends BaseRouteAnalyzer {
  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    new BaseRouteWithoutWaysAnalyzer(context).analyze
  }
}

class BaseRouteWithoutWaysAnalyzer(context: BaseRouteAnalysisContext) {

  def analyze: BaseRouteAnalysisContext = {
    if (hasWays || isSuperRoute || hasRelationMembers) {
      context
    }
    else {
      context.withFact(RouteWithoutWays)
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
