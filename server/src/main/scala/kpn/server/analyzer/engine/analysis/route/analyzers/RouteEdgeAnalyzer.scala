package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.route.RouteEdge
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.domain.StructurePath

object RouteEdgeAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new RouteEdgeAnalyzer(context).analyze
  }
}

class RouteEdgeAnalyzer(context: RouteDetailAnalysisContext) {

  def analyze: RouteDetailAnalysisContext = {
    val edges = if (context.nodeNetwork) {
      context.structure.nodeNetworkPaths.map(toEdge)
    }
    else {
      Seq.empty
    }
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
