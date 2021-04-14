package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.server.analyzer.engine.analysis.route.domain.RouteNodeInfo
import kpn.server.analyzer.load.data.LoadedRoute

trait RouteNodeInfoAnalyzer {

  def analyze(loadedRoute: LoadedRoute): Map[Long, RouteNodeInfo]

}
