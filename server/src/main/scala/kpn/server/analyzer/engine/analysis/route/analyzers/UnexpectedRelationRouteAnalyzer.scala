package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact.RouteUnexpectedRelation
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

object UnexpectedRelationRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new UnexpectedRelationRouteAnalyzer(context).analyze
  }
}

class UnexpectedRelationRouteAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    val relationIds = findUnexpectedRelationIds
    context.copy(unexpectedRelationIds = Some(relationIds)).withFact(relationIds.nonEmpty, RouteUnexpectedRelation)
  }

  private def findUnexpectedRelationIds: Seq[Long] = {
    context.loadedRoute.relation.relationMembers.map(_.relation.id)
  }

}
