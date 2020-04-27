package kpn.server.analyzer.engine.analysis.network

import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.load.data.LoadedNetwork

trait NetworkRouteAnalyzer {
  def analyze(networkRelationAnalysis: NetworkRelationAnalysis, loadedNetwork: LoadedNetwork): Map[Long, RouteAnalysis]
}
