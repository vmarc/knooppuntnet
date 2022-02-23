package kpn.server.analyzer.engine.tile

import kpn.api.common.tiles.ZoomLevel
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.engine.tiles.domain.ZoomLevelRouteTileSegments
import org.springframework.stereotype.Component

@Component
class RouteTileChangeAnalyzerImpl() extends RouteTileChangeAnalyzer {

  def impactedTiles(before: RouteAnalysis, after: RouteAnalysis): Seq[String] = {
    if (tileRelatedRoutePropertiesChanged(before, after)) {
      // all tiles before and after are impacted
      (before.route.tiles ++ after.route.tiles).distinct.sorted
    }
    else {
      ZoomLevel.all.flatMap { zoomLevel =>
        if (segmentsIn(before, zoomLevel) != segmentsIn(after, zoomLevel)) {
          (tiles(before, zoomLevel) ++ tiles(after, zoomLevel)).distinct.sorted
        }
        else {
          Seq.empty
        }
      }
    }
  }

  private def tileRelatedRoutePropertiesChanged(before: RouteAnalysis, after: RouteAnalysis): Boolean = {
    val networkTypeBefore = before.route.summary.networkType
    val networkTypeAfter = after.route.summary.networkType
    !(networkTypeBefore == networkTypeAfter && before.tileAnalysis.sameProperties(after.tileAnalysis))
  }

  private def segmentsIn(routeAnalysis: RouteAnalysis, zoomLevel: Int): Seq[ZoomLevelRouteTileSegments] = {
    routeAnalysis.tileAnalysis.zoomLevelSegments.filter(_.zoomLevel == zoomLevel)
  }

  private def tiles(routeAnalysis: RouteAnalysis, zoomLevel: Int): Seq[String] = {
    routeAnalysis.route.tiles.filter(z => TileName.tileZoomLevel(z) == zoomLevel)
  }
}
