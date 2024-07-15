package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.domain.OldTile
import kpn.server.analyzer.engine.tiles.domain.RouteTileSegment
import org.springframework.stereotype.Component

@Component
class RouteTileCalculatorImpl(lineSegmentTileCalculator: LineSegmentTileCalculator) extends RouteTileCalculator {

  override def tiles(z: Int, segments: Seq[RouteTileSegment]): Seq[OldTile] = {
    // TODO redesign - cleanup
    //  val lines = segments.flatMap(_.lineSegments)
    //  linesTileCalculator.tiles(z, lines)
    Seq.empty
  }
}
