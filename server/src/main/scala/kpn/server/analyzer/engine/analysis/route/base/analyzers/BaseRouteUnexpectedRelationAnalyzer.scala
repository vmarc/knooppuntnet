package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact.RouteUnexpectedRelation

object BaseRouteUnexpectedRelationAnalyzer extends BaseRouteAnalyzer {
  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    new BaseRouteUnexpectedRelationAnalyzer(context).analyze
  }
}

class BaseRouteUnexpectedRelationAnalyzer(context: BaseRouteAnalysisContext) {

  def analyze: BaseRouteAnalysisContext = {
    val relationIds = findUnexpectedRelationIds
    context.copy(
      _unexpectedRelationIds = Some(relationIds)
    ).withFact(relationIds.nonEmpty, RouteUnexpectedRelation)
  }

  private def findUnexpectedRelationIds: Seq[Long] = {
    context.relation.relationMembers.map(_.relation.id)
  }
}
