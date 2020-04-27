package kpn.server.analyzer.engine.analysis.network

import kpn.core.analysis.NetworkNode
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.load.data.LoadedNetwork

trait NetworkRouteAnalyzer {
  def analyze(allNodes: Map[Long, NetworkNode], networkRelationAnalysis: NetworkRelationAnalysis, loadedNetwork: LoadedNetwork): Map[Long, RouteAnalysis]
}
