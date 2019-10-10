package kpn.core.engine.analysis.route

import kpn.core.analysis.NetworkNode
import kpn.core.load.data.LoadedRoute

trait MasterRouteAnalyzer {
  def analyze(networkNodes: Map[Long, NetworkNode], loadedRoute: LoadedRoute, orphan: Boolean): RouteAnalysis
}
