package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.tiles.ZoomLevel
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileCalculator
import kpn.server.analyzer.engine.tiles.TileDataRouteBuilder
import kpn.server.analyzer.engine.tiles.domain.RouteTileInfo
import org.springframework.stereotype.Component

@Component
class RouteTileAnalyzer(routeTileCalculator: RouteTileCalculator) extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    val tiles = (ZoomLevel.minZoom to ZoomLevel.maxZoom).flatMap { zoomLevel =>
      context.routeMap.toSeq.flatMap { routeMap =>
        val name = context.routeNameAnalysis match {
          case None => "no-name"
          case Some(routeNameAnalysis) =>
            routeNameAnalysis.name match {
              case None => "no-name"
              case Some(name) => name
            }
        }

        val routeTileInfo = RouteTileInfo(
          _id = context.relation.id,
          name = name,
          proposed = context.proposed,
          lastSurvey = context.lastSurvey,
          tags = context.relation.tags,
          facts = context.facts,
          freePaths = routeMap.freePaths,
          forwardPath = routeMap.forwardPath,
          backwardPath = routeMap.backwardPath,
          startTentaclePaths = routeMap.startTentaclePaths,
          endTentaclePaths = routeMap.endTentaclePaths,
        )
        new TileDataRouteBuilder(zoomLevel).fromRouteInfo(routeTileInfo) match {
          case None => Seq.empty
          case Some(tileRouteData) =>
            val tiles = routeTileCalculator.tiles(zoomLevel, tileRouteData)
            tiles.map(tile => s"${context.scopedNetworkType.networkType.name}-${tile.name}")
        }
      }
    }
    context.copy(tiles = tiles)
  }
}
