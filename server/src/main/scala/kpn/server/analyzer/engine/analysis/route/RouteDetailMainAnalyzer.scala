package kpn.server.analyzer.engine.analysis.route

import kpn.api.custom.Relation
import kpn.core.tools.next.domain.RouteRelation

trait RouteDetailMainAnalyzer {
  def analyze(relation: Relation, hierarchy: Option[RouteRelation]): Option[RouteDetailAnalysis]
}
