package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.tiles.domain.ZoomLevelRouteTileSegments
import org.springframework.stereotype.Component

@Component
class RouteTileChangeAnalyzerImpl extends RouteTileChangeAnalyzer {

  def impactedTiles(before: RouteDetailAnalysisContext, after: RouteDetailAnalysisContext): Seq[String] = {
    Seq.empty // TODO redesign, re-implement
    //    if (tileRelatedRoutePropertiesChanged(before, after)) {
    //      // all tiles before and after are impacted
    //      (before.tiles ++ after.tiles).distinct.sorted
    //    }
    //    else {
    //      ZoomLevel.all.flatMap { zoomLevel =>
    //        if (segmentsIn(before, zoomLevel) != segmentsIn(after, zoomLevel)) {
    //          (tiles(before, zoomLevel) ++ tiles(after, zoomLevel)).distinct.sorted
    //        }
    //        else {
    //          Seq.empty
    //        }
    //      }
    //    }
  }

  private def tileRelatedRoutePropertiesChanged(before: RouteDetailAnalysisContext, after: RouteDetailAnalysisContext): Boolean = {
    val networkTypeBefore = before.networkTypes.head // TODO redesign - support multiple network types
    val networkTypeAfter = after.networkTypes.head
    !(networkTypeBefore == networkTypeAfter && before.tileAnalysis.sameProperties(after.tileAnalysis))
  }

  private def segmentsIn(context: RouteDetailAnalysisContext, zoomLevel: Int): Seq[ZoomLevelRouteTileSegments] = {
    context.tileAnalysis.zoomLevelSegments.filter(_.zoomLevel == zoomLevel)
  }

  private def tiles(context: RouteDetailAnalysisContext, zoomLevel: Int): Seq[String] = {
    context.tiles.filter(z => TileName.tileZoomLevel(z) == zoomLevel)
  }
}
