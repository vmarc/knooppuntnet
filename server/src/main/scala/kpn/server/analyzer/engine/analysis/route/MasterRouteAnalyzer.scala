package kpn.server.analyzer.engine.analysis.route

import kpn.api.custom.Relation

trait MasterRouteAnalyzer {
  def analyze(relation: Relation): RouteAnalysis
}
