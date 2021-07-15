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
      context.routeMap.toSeq.flatMap { routeMap =>
        val name = context.routeNameAnalysis match {
          case None => "no-name"
          case Some(routeNameAnalysis) =>
            routeNameAnalysis.name match {
              case None => "no-name"
              case Some(name) => name
            }
        }
        new TileDataRouteBuilder(zoomLevel).from(
          context.loadedRoute.id,
          name,
          context.loadedRoute.relation.tags,
          routeMap,
          context.orphan,
          context.facts
        ) match {
          case None => Seq.empty
          case Some(tileRouteData) =>
            val tiles = routeTileCalculator.tiles(zoomLevel, tileRouteData)
            tiles.map(tile => s"${context.loadedRoute.scopedNetworkType.networkType.name}-${tile.name}")
        }
      }
    }
    context.copy(tiles = tiles)
  }
}
