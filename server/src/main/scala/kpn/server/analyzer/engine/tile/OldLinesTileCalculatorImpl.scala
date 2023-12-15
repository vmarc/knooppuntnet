package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.domain.Line
import kpn.server.analyzer.engine.tiles.domain.Point
import kpn.server.analyzer.engine.tiles.domain.OldTile
import org.springframework.stereotype.Component

@Component
class OldLinesTileCalculatorImpl(tileCalculator: OldTileCalculator) extends OldLinesTileCalculator {

  override def tiles(z: Int, lines: Seq[Line]): Seq[OldTile] = {

    val tileQueue = scala.collection.mutable.Queue[OldTile]()
    val foundTiles = scala.collection.mutable.Set[OldTile]()

    val tiles = lines.flatMap(_.points).map { p: Point =>
      tileCalculator.tileLonLat(z, p.x, p.y)
    }.toSet

    foundTiles ++= tiles.toSeq
    tileQueue ++= tiles

    while (tileQueue.nonEmpty) {
      val tile = tileQueue.dequeue()
      explore(tileQueue, foundTiles, lines, tile, tile.bounds.left, -1, 0)
      explore(tileQueue, foundTiles, lines, tile, tile.bounds.right, 1, 0)
      explore(tileQueue, foundTiles, lines, tile, tile.bounds.top, 0, 1)
      explore(tileQueue, foundTiles, lines, tile, tile.bounds.bottom, 0, -1)
    }
    foundTiles.toSeq
  }

  private def explore(
    tileQueue: scala.collection.mutable.Queue[OldTile],
    foundTiles: scala.collection.mutable.Set[OldTile],
    lines: Seq[Line],
    tile: OldTile,
    side: Line,
    xDelta: Int,
    yDelta: Int
  ): Unit = {
    val adjecentTile = tileCalculator.tileXY(tile.z, tile.x + xDelta, tile.y + yDelta)
    if (!foundTiles.map(_.name).contains(adjecentTile.name)) {
      if (lines.exists(_.intersects(side))) {
        tileQueue += adjecentTile
        foundTiles += adjecentTile
        ()
      }
    }
  }
}
