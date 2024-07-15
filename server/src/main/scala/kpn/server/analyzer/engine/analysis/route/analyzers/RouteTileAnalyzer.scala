package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Relation
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculator
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.wayToWorldCoordinates
import kpn.server.analyzer.engine.tiles.domain.Tile
import org.locationtech.jts.geom.LineSegment
import org.springframework.stereotype.Component

@Component
class RouteTileAnalyzer(lineSegmentTileCalculator: LineSegmentTileCalculator) extends RouteAnalyzer {

  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    val tiles = determineTiles(context.relation).map(_.name)
    context.copy(
      tiles = tiles
    )
  }

  private def determineTiles(relation: Relation): Seq[Tile] = {
    relation.wayMembers.map(_.way).flatMap { way =>
      val worldCoordinates = wayToWorldCoordinates(way)
      val lineSegments = worldCoordinates.sliding(2).map { case Seq(c1, c2) =>
        new LineSegment(c1, c2)
      }.toSeq
      (2 to 14).flatMap { z =>
        lineSegmentTileCalculator.tiles(z, lineSegments)
      }
    }.distinct.sortBy(tile => (tile.z, tile.x, tile.y))
  }
}
