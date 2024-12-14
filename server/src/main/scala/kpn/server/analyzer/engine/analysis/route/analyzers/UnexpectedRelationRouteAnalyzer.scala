package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.Fact.RouteUnexpectedRelation
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object UnexpectedRelationRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new UnexpectedRelationRouteAnalyzer(context).analyze
  }
}

class UnexpectedRelationRouteAnalyzer(context: RouteDetailAnalysisContext) {

  def analyze: RouteDetailAnalysisContext = {
    val relationIds = findUnexpectedRelationIds
    context.copy(
      _unexpectedRelationIds = Some(relationIds)
    ).withFact(relationIds.nonEmpty, RouteUnexpectedRelation)
  }

  private def findUnexpectedRelationIds: Seq[Long] = {
    context.relation.relationMembers.map(_.relation.id)
  }
}
