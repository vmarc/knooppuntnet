package kpn.server.analyzer.engine.analysis.route

import kpn.core.analysis.NetworkNode
import kpn.server.analyzer.load.data.LoadedRoute

trait MasterRouteAnalyzer {
  def analyze(networkNodes: Map[Long, NetworkNode], loadedRoute: LoadedRoute, orphan: Boolean): RouteAnalysis
}
