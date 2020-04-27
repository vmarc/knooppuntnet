package kpn.server.analyzer.engine.analysis.route

import kpn.server.analyzer.load.data.LoadedRoute

trait MasterRouteAnalyzer {
  def analyze(loadedRoute: LoadedRoute, orphan: Boolean): RouteAnalysis
}
