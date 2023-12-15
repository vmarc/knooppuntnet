package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileUtil
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.LineSegment
import org.springframework.stereotype.Component

@Component
class LineSegmentTileCalculatorImpl(tileCalculator: TileCalculator) extends LineSegmentTileCalculator {

  override def tiles(z: Int, lineSegments: Seq[LineSegment]): Seq[Tile] = {

    val tileQueue = scala.collection.mutable.Queue[Tile]()
    val foundTiles = scala.collection.mutable.Set[Tile]()

    val tiles = lineSegments.flatMap(ls => Seq(ls.p0, ls.p1)).map { p: Coordinate =>
      tileCalculator.tileContainingWorldCoordinate(z, p.x, p.y)
    }.toSet

    foundTiles ++= tiles.toSeq
    tileQueue ++= tiles

    while (tileQueue.nonEmpty) {
      val tile = tileQueue.dequeue()
      explore(tileQueue, foundTiles, lineSegments, tile, TileUtil.left(tile), -1, 0)
      explore(tileQueue, foundTiles, lineSegments, tile, TileUtil.right(tile), 1, 0)
      explore(tileQueue, foundTiles, lineSegments, tile, TileUtil.top(tile), 0, 1)
      explore(tileQueue, foundTiles, lineSegments, tile, TileUtil.bottom(tile), 0, -1)
    }
    foundTiles.toSeq
  }

  private def explore(
    tileQueue: scala.collection.mutable.Queue[Tile],
    foundTiles: scala.collection.mutable.Set[Tile],
    lineSegments: Seq[LineSegment],
    tile: Tile,
    side: LineSegment,
    xDelta: Int,
    yDelta: Int
  ): Unit = {
    val x = tile.x + xDelta
    val y = tile.y + yDelta
    if (x >= 0 && y >= 0) {
      val adjecentTile = tileCalculator.tileXY(tile.z, x, y)
      if (!foundTiles.map(_.name).contains(adjecentTile.name)) {
        if (lineSegments.exists(_.intersection(side) != null)) {
          tileQueue += adjecentTile
          foundTiles += adjecentTile
          ()
        }
      }
    }
  }
}
