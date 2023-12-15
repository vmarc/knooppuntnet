package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.domain.Line
import kpn.server.analyzer.engine.tiles.domain.OldTile

trait OldLinesTileCalculator {

  def tiles(z: Int, lines: Seq[Line]): Seq[OldTile]
}
