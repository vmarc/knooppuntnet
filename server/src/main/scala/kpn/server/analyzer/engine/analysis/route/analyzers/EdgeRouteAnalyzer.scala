package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.common.TrackPath
import kpn.api.common.route.RouteEdge
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

object EdgeRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new EdgeRouteAnalyzer(context).analyze
  }
}

class EdgeRouteAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {

    context.routeMap match {
      case None => context
      case Some(routeMap) =>
        val edges = Seq(
          toEdges(routeMap.forwardPath),
          toEdges(routeMap.backwardPath),
          routeMap.freePaths.map(toEdge),
          routeMap.startTentaclePaths.map(toEdge),
          routeMap.endTentaclePaths.map(toEdge),
        ).flatten
        context.copy(edges = edges)
    }
  }

  private def toEdges(trackPathOption: Option[TrackPath]): Seq[RouteEdge] = {
    trackPathOption match {
      case None => Seq.empty
      case Some(trackPath) =>
        if (trackPath.oneWay) {
          Seq(toEdge(trackPath))
        }
        else {
          Seq(
            toEdge(trackPath),
            toReverseEdge(trackPath)
          )
        }
    }
  }

  private def toEdge(trackPath: TrackPath): RouteEdge = {
    RouteEdge(
      trackPath.pathId,
      trackPath.startNodeId,
      trackPath.endNodeId,
      trackPath.meters
    )
  }

  private def toReverseEdge(trackPath: TrackPath): RouteEdge = {
    RouteEdge(
      100L + trackPath.pathId,
      trackPath.endNodeId,
      trackPath.startNodeId,
      trackPath.meters
    )
  }
}
