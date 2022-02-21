package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.domain.Line
import kpn.server.analyzer.engine.tiles.domain.Point
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.RouteTileSegment
import org.springframework.stereotype.Component

@Component
class RouteTileCalculatorImpl(tileCalculator: TileCalculator) extends RouteTileCalculator {

  override def tiles(z: Int, segments: Seq[RouteTileSegment]): Seq[Tile] = {

    val tileQueue = scala.collection.mutable.Queue[Tile]()
    val foundTiles = scala.collection.mutable.Set[Tile]()

    val lines = segments.flatMap(_.lines)

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
    tileQueue: scala.collection.mutable.Queue[Tile],
    foundTiles: scala.collection.mutable.Set[Tile],
    lines: Seq[Line],
    tile: Tile,
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
