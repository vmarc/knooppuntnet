package kpn.server.analyzer.engine.analysis.route

import kpn.api.custom.Relation
import kpn.core.tools.next.domain.RouteRelation
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

trait RouteDetailMainAnalyzer {
  def analyze(relation: Relation, hierarchy: Option[RouteRelation], traceEnabled: Boolean = false): Option[RouteDetailAnalysisContext]
}
