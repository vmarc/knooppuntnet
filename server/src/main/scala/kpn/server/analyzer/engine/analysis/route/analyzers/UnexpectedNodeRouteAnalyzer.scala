package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.data.Node
import kpn.api.custom.Fact.RouteUnexpectedNode
import kpn.core.analysis.TagInterpreter
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object UnexpectedNodeRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new UnexpectedNodeRouteAnalyzer(context).analyze
  }
}

class UnexpectedNodeRouteAnalyzer(context: RouteDetailAnalysisContext) {

  def analyze: RouteDetailAnalysisContext = {
    val unexpectedNodeIds = findUnexpectedNodeIds
    context.copy(
        _unexpectedNodeIds = Some(unexpectedNodeIds)
      ).withFact(unexpectedNodeIds.nonEmpty, RouteUnexpectedNode)
      .withOldFact(unexpectedNodeIds.nonEmpty, RouteUnexpectedNode)
  }

  private def findUnexpectedNodeIds: Seq[Long] = {
    routeNodes.filter(n => TagInterpreter.isUnexpectedNode(context.scopedNetworkType, n)).map(_.id)
  }

  private def routeNodes: Seq[Node] = {
    context.relation.nodeMembers.map(_.node)
  }
}
