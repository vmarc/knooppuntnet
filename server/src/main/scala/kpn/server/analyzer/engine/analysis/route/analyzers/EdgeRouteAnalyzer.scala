package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.route.RouteEdge
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.structure.StructurePath

object EdgeRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new EdgeRouteAnalyzer(context).analyze
  }
}

class EdgeRouteAnalyzer(context: RouteDetailAnalysisContext) {

  def analyze: RouteDetailAnalysisContext = {
    val edges = context.structure.nodeNetworkPaths.map(toEdge)
    context.copy(edges = edges)
  }

  private def toEdge(path: StructurePath): RouteEdge = {
    RouteEdge(
      path.id,
      path.startNodeId,
      path.endNodeId,
      path.meters
    )
  }
}
