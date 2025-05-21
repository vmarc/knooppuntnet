package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.custom.Relation
import kpn.server.analyzer.engine.analysis.route.domain.RouteLinks
import kpn.server.analyzer.engine.analysis.route.structure.reference.JavaLinkAnalyzer

object BaseRouteLinkAnalyzer extends BaseRouteAnalyzer {
  override def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    val links = new BaseRouteLinkAnalyzer(context.traceEnabled).analyze(context.relation)
    context.copy(
      _links = Some(links)
    )
  }
}

class BaseRouteLinkAnalyzer(traceEnabled: Boolean = false) {
  def analyze(relation: Relation): RouteLinks = {
    val javaRelation = JavaRelationConverter.toJava(relation)
    val javaLinks = JavaLinkAnalyzer.analyze(javaRelation.getMembers, traceEnabled)
    JavaLinkConverter.toScala(relation, javaLinks)
  }
}
