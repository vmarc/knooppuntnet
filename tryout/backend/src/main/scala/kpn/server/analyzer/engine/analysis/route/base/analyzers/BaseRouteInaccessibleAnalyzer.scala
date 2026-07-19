package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact.RouteInaccessible
import kpn.api.common.RouteMemberInfo
import kpn.core.util.Log

object BaseRouteInaccessibleAnalyzer extends BaseRouteAnalyzer {
  private val log = Log(classOf[BaseRouteInaccessibleAnalyzer])

  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    new BaseRouteInaccessibleAnalyzer(context).analyze
  }
}

class BaseRouteInaccessibleAnalyzer(context: BaseRouteAnalysisContext) {

  def analyze: BaseRouteAnalysisContext = {
    if (context.routeMembers.exists(hasInaccessibleWay)) {
      context.withFact(RouteInaccessible)
    }
    else {
      context
    }
  }

  private def hasInaccessibleWay(member: RouteMemberInfo): Boolean = {
    member.way.exists(!_.accessible)
  }
}
