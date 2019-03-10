package kpn.core.engine.analysis.route.analyzers

import kpn.core.analysis.RouteMemberWay
import kpn.core.engine.analysis.route.RouteAnalyzerFunctions
import kpn.core.engine.analysis.route.RouteNodeAnalysis
import kpn.core.engine.analysis.route.RouteStructure
import kpn.core.engine.analysis.route.domain.RouteAnalysisContext
import kpn.shared.common.MapBounds
import kpn.shared.data.Way
import kpn.shared.route.RouteMap

object RouteMapAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RouteMapAnalyzer(context).analyze
  }
}

class RouteMapAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {

    val ways: Seq[Way] = context.routeMembers.get.flatMap {
      case w: RouteMemberWay => Some(w.way)
      case _ => None
    }

    val allWayNodes = ways.flatMap(w => w.nodes)
    val bounds = MapBounds(allWayNodes ++ context.routeNodeAnalysis.get.routeNodes.map(_.node))
    val routeMap = buildRouteMap(context.routeNodeAnalysis.get, context.structure.get, bounds)
    context.copy(ways = Some(ways), allWayNodes = Some(allWayNodes), routeMap = Some(routeMap))
  }

  private def buildRouteMap(routeNodeAnalysis: RouteNodeAnalysis, structure: RouteStructure, bounds: MapBounds): RouteMap = {
    val forwardBreakPoint = {
      structure.forwardPath match {
        case Some(path) if path.broken => Some(RouteAnalyzerFunctions.toTrackPoint(path.segments.last.nodes.last))
        case _ => None
      }
    }

    val backwardBreakPoint = {
      structure.backwardPath match {
        case Some(path) if path.broken => Some(RouteAnalyzerFunctions.toTrackPoint(path.segments.last.nodes.last))
        case _ => None
      }
    }

    RouteMap(
      bounds,
      forwardPath = structure.forwardPath.map(RouteAnalyzerFunctions.toTrackPath),
      backwardPath = structure.backwardPath.map(RouteAnalyzerFunctions.toTrackPath),
      unusedPaths = structure.unusedPaths.map(RouteAnalyzerFunctions.toTrackPath),
      startTentaclePaths = structure.startTentaclePaths.map(RouteAnalyzerFunctions.toTrackPath),
      endTentaclePaths = structure.endTentaclePaths.map(RouteAnalyzerFunctions.toTrackPath),
      forwardBreakPoint = forwardBreakPoint,
      backwardBreakPoint = backwardBreakPoint,
      startNodes = RouteAnalyzerFunctions.toInfos(if (routeNodeAnalysis.startNodes.isEmpty) Seq() else Seq(routeNodeAnalysis.startNodes.head)),
      endNodes = RouteAnalyzerFunctions.toInfos(if (routeNodeAnalysis.endNodes.isEmpty) Seq() else Seq(routeNodeAnalysis.endNodes.head)),
      startTentacleNodes = RouteAnalyzerFunctions.toInfos(if (routeNodeAnalysis.startNodes.size <= 1) Seq() else routeNodeAnalysis.startNodes.tail),
      endTentacleNodes = RouteAnalyzerFunctions.toInfos(if (routeNodeAnalysis.endNodes.size <= 1) Seq() else routeNodeAnalysis.endNodes.tail),
      redundantNodes = RouteAnalyzerFunctions.toInfos(routeNodeAnalysis.redundantNodes)
    )
  }

}
