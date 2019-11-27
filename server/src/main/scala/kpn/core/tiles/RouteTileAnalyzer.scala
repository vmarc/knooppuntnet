package kpn.core.tiles

import kpn.core.tiles.domain.Line
import kpn.core.tiles.domain.Point
import kpn.core.tiles.domain.Tile
import kpn.core.tiles.domain.TileCache
import kpn.core.tiles.domain.TileDataRoute

class RouteTileAnalyzer(tileCache: TileCache) {

  /*
    Determines all tiles that will be needed to display given route
    at given zoom level.
   */
  def tiles(z: Int, tileRoute: TileDataRoute): Seq[Tile] = {

    val tileQueue = scala.collection.mutable.Queue[Tile]()
    val foundTiles = scala.collection.mutable.Set[Tile]()

    val lines = tileRoute.segments.flatMap(_.lines)

    val tiles = lines.flatMap(_.points).map { p: Point =>
      tileCache(z, p.x, p.y)
    }.toSet

    foundTiles ++= tiles.toSeq
    tileQueue ++= tiles

    while (tileQueue.nonEmpty) {
      val tile = tileQueue.dequeue()
      explore(tileQueue, foundTiles, lines, tile, tile.bounds.left, -1, 0)
      explore(tileQueue, foundTiles, lines, tile, tile.bounds.right, 1, 0)
      explore(tileQueue, foundTiles, lines, tile, tile.bounds.top, 0, -1)
      explore(tileQueue, foundTiles, lines, tile, tile.bounds.bottom, 0, 1)
    }
    foundTiles.toSeq
  }

  private def explore(tileQueue: scala.collection.mutable.Queue[Tile], foundTiles: scala.collection.mutable.Set[Tile], lines: Seq[Line], tile: Tile, side: Line,
    xDelta: Int, yDelta: Int): Unit = {
    val adjecentTile = tileCache(tile.z, tile.x + xDelta, tile.y + yDelta)
    if (!foundTiles.contains(adjecentTile)) {
      if (lines.exists(_.intersects(side))) {
        tileQueue += adjecentTile
        foundTiles += adjecentTile
        ()
      }
    }
  }
}
