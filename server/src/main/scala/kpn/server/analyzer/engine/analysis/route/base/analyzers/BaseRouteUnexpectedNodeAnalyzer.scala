package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact.RouteUnexpectedNode
import kpn.api.common.data.Node
import kpn.core.analysis.TagInterpreter

object BaseRouteUnexpectedNodeAnalyzer extends BaseRouteAnalyzer {
  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    new BaseRouteUnexpectedNodeAnalyzer(context).analyze
  }
}

class BaseRouteUnexpectedNodeAnalyzer(context: BaseRouteAnalysisContext) {

  def analyze: BaseRouteAnalysisContext = {
    val unexpectedNodeIds = if (context.nodeNetwork) {
      findUnexpectedNodeIds
    }
    else {
      Seq.empty
    }
    context.copy(
      _unexpectedNodeIds = Some(unexpectedNodeIds)
    ).withFact(unexpectedNodeIds.nonEmpty, RouteUnexpectedNode)
  }

  private def findUnexpectedNodeIds: Seq[Long] = {
    routeNodes.filter(n => TagInterpreter.isUnexpectedNode(context.scopedRouteType, n)).map(_.id)
  }

  private def routeNodes: Seq[Node] = {
    context.relation.nodeMembers.map(_.node)
  }
}
