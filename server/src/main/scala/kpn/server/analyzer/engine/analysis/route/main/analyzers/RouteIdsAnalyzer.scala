package kpn.server.analyzer.engine.analysis.route.main.analyzers

import kpn.core.doc.RouteRelation
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

object RouteIdsAnalyzer extends RouteAnalyzer {
  override def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    val routeIds = context.route.hierarchy match {
      case Some(rootRelation) => RouteRelation.relationIds(rootRelation)
      case None => Seq(context.route.id)
    }
    context.copy(_routeIds = Some(routeIds))
  }
}
