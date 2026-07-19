package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.route.RouteEdge
import kpn.server.analyzer.engine.analysis.route.domain.StructurePath

object BaseRouteEdgeAnalyzer extends BaseRouteAnalyzer {
  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    new BaseRouteEdgeAnalyzer(context).analyze
  }
}

class BaseRouteEdgeAnalyzer(context: BaseRouteAnalysisContext) {

  def analyze: BaseRouteAnalysisContext = {
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
