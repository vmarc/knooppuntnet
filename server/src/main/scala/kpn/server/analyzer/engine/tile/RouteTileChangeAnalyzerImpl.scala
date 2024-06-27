package kpn.server.analyzer.engine.tile

import kpn.api.common.tiles.ZoomLevel
import kpn.server.analyzer.engine.analysis.route.RouteDetailAnalysis
import kpn.server.analyzer.engine.tiles.domain.ZoomLevelRouteTileSegments
import org.springframework.stereotype.Component

@Component
class RouteTileChangeAnalyzerImpl extends RouteTileChangeAnalyzer {

  def impactedTiles(before: RouteDetailAnalysis, after: RouteDetailAnalysis): Seq[String] = {
    if (tileRelatedRoutePropertiesChanged(before, after)) {
      // all tiles before and after are impacted
      (before.routeDetail.tiles ++ after.routeDetail.tiles).distinct.sorted
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

  private def tileRelatedRoutePropertiesChanged(before: RouteDetailAnalysis, after: RouteDetailAnalysis): Boolean = {
    val networkTypeBefore = before.routeDetail.summary.networkType
    val networkTypeAfter = after.routeDetail.summary.networkType
    !(networkTypeBefore == networkTypeAfter && before.tileAnalysis.sameProperties(after.tileAnalysis))
  }

  private def segmentsIn(routeAnalysis: RouteDetailAnalysis, zoomLevel: Int): Seq[ZoomLevelRouteTileSegments] = {
    routeAnalysis.tileAnalysis.zoomLevelSegments.filter(_.zoomLevel == zoomLevel)
  }

  private def tiles(routeAnalysis: RouteDetailAnalysis, zoomLevel: Int): Seq[String] = {
    routeAnalysis.routeDetail.tiles.filter(z => TileName.tileZoomLevel(z) == zoomLevel)
  }
}
