package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.domain.OldTile
import kpn.server.analyzer.engine.tiles.domain.RouteTileSegment
import org.springframework.stereotype.Component

@Component
class RouteTileCalculatorImpl(linesTileCalculator: OldLinesTileCalculator) extends RouteTileCalculator {

  override def tiles(z: Int, segments: Seq[RouteTileSegment]): Seq[OldTile] = {
    val lines = segments.flatMap(_.lines)
    linesTileCalculator.tiles(z, lines)
  }
}
