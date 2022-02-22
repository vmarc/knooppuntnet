package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.tiles.ZoomLevel
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.context.PreconditionMissingException
import kpn.server.analyzer.engine.tile.RouteTileCalculator
import kpn.server.analyzer.engine.tiles.TileDataRouteBuilder
import kpn.server.analyzer.engine.tiles.domain.RouteTileAnalysis
import kpn.server.analyzer.engine.tiles.domain.RouteTileInfo
import kpn.server.analyzer.engine.tiles.domain.ZoomLevelRouteTileSegments
import org.springframework.stereotype.Component

@Component
class RouteTileAnalyzer(routeTileCalculator: RouteTileCalculator) extends RouteAnalyzer {

  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {

    val routeTileInfo = toRouteTileInfo(context)
    val zoomLevelAndTileDataRoutes = ZoomLevel.all.flatMap { zoomLevel =>
      new TileDataRouteBuilder(zoomLevel).fromRouteInfo(routeTileInfo).map { tileDataRoute =>
        zoomLevel -> tileDataRoute
      }
    }

    val zoomLevelSegmentss = zoomLevelAndTileDataRoutes.map { tileDataRoute =>
      ZoomLevelRouteTileSegments(tileDataRoute._1, tileDataRoute._2.segments)
    }

    val tiles = zoomLevelAndTileDataRoutes.flatMap { tileDataRoute =>
      val tiles = routeTileCalculator.tiles(tileDataRoute._1, tileDataRoute._2.segments)
      tiles.map(tile => s"${context.scopedNetworkType.networkType.name}-${tile.name}")
    }

    val tileAnalysis = RouteTileAnalysis(
      routeName = zoomLevelAndTileDataRoutes.head._2.routeName,
      layer = zoomLevelAndTileDataRoutes.head._2.layer,
      surveyDate = zoomLevelAndTileDataRoutes.head._2.surveyDate,
      state = zoomLevelAndTileDataRoutes.head._2.state,
      zoomLevelSegments = zoomLevelSegmentss
    )

    context.copy(
      tileAnalysis = Some(tileAnalysis),
      tiles = tiles
    )
  }

  private def routeName(context: RouteAnalysisContext): String = {
    context.routeNameAnalysis match {
      case None => "no-name"
      case Some(routeNameAnalysis) =>
        routeNameAnalysis.name match {
          case None => "no-name"
          case Some(name) => name
        }
    }
  }

  private def toRouteTileInfo(context: RouteAnalysisContext): RouteTileInfo = {
    val routeMap = context.routeMap.getOrElse(throw new PreconditionMissingException)
    RouteTileInfo(
      context.relation.id,
      routeName(context),
      context.proposed,
      context.lastSurvey,
      context.relation.tags,
      context.facts,
      routeMap.freePaths,
      routeMap.forwardPath,
      routeMap.backwardPath,
      routeMap.startTentaclePaths,
      routeMap.endTentaclePaths
    )
  }
}
