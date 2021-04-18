package kpn.core.tools.route

import kpn.server.analyzer.engine.analysis.route.RouteAnalysis

trait RouteFileAnalyzer {
  def analyze(routeId: Long): Option[RouteAnalysis]
}
