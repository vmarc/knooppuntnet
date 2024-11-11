package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.domain.OldTile
import kpn.server.analyzer.engine.tiles.domain.TileDataRouteSegment
import org.springframework.stereotype.Component

@Component
class RouteTileCalculatorImpl(lineSegmentTileCalculator: LineSegmentTileCalculator) extends RouteTileCalculator {

  override def tiles(z: Int, segments: Seq[TileDataRouteSegment]): Seq[OldTile] = {
    // TODO redesign tiles - cleanup
    //  val lines = segments.flatMap(_.lineSegments)
    //  linesTileCalculator.tiles(z, lines)
    Seq.empty
  }
}
