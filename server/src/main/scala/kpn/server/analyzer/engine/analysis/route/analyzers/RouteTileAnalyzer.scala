package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.tiles.ZoomLevel
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileCalculator
import kpn.server.analyzer.engine.tiles.TileDataRouteBuilder
import org.springframework.stereotype.Component

@Component
class RouteTileAnalyzer(routeTileCalculator: RouteTileCalculator) extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    val tiles = (ZoomLevel.minZoom to ZoomLevel.maxZoom).flatMap { zoomLevel =>
      new TileDataRouteBuilder(zoomLevel).from(
        context.loadedRoute.id,
        context.routeNameAnalysis.get.name.get,
        context.loadedRoute.relation.tags,
        context.routeMap.get,
        context.orphan,
        context.facts
      ) match {
        case None => Seq.empty
        case Some(tileRouteData) =>
          val tiles = routeTileCalculator.tiles(zoomLevel, tileRouteData)
          tiles.map(tile => s"${context.loadedRoute.scopedNetworkType.networkType.name}-${tile.name}")
      }
    }
    context.copy(tiles = tiles)
  }
}
