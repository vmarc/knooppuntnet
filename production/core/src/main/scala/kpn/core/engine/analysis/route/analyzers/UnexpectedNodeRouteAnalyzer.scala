package kpn.core.engine.analysis.route.analyzers

import kpn.core.engine.analysis.route.domain.RouteAnalysisContext
import kpn.shared.Fact.RouteUnexpectedNode
import kpn.shared.data.Node

object UnexpectedNodeRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new UnexpectedNodeRouteAnalyzer(context).analyze
  }
}

class UnexpectedNodeRouteAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    val unexpectedNodeIds = findUnexpectedNodeIds
    context.copy(unexpectedNodeIds = Some(unexpectedNodeIds)).withFact(unexpectedNodeIds.nonEmpty, RouteUnexpectedNode)
  }

  private def findUnexpectedNodeIds: Seq[Long] = {
    // TODO ROUTE could/should move isUnexpectedNode into this class???
    routeNodes.filter(n => context.analysisContext.isUnexpectedNode(context.networkType, n)).map(_.id)
  }

  private def routeNodes: Seq[Node] = {
    context.loadedRoute.relation.nodeMembers.map(_.node)
  }
}
