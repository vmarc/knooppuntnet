package kpn.server.analyzer.engine.analysis.route

import kpn.api.custom.Relation

trait MainRouteAnalyzer {
  def analyze(relation: Relation): Option[RouteAnalysis]
}
