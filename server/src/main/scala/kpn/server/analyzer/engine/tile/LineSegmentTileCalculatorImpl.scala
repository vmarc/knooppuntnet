package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.domain.Tile
import org.locationtech.jts.geom.LineSegment
import org.springframework.stereotype.Component

/*
  Calculates which map tiles are needed to display a set of line segments at a given zoom level.
 */
@Component
class LineSegmentTileCalculatorImpl(routeTileCache: RouteTileCache) extends LineSegmentTileCalculator {

  override def tiles(z: Int, lineSegments: Seq[LineSegment]): Seq[Tile] = {

    val tileQueue = scala.collection.mutable.Queue[Tile]()
    val foundTiles = scala.collection.mutable.Map[String, Tile]()

    // the tiles of the end points of the segments are the starting point for exploration
    lineSegments.flatMap(ls => Seq(ls.p0, ls.p1)).map { p =>
      val x = Tile.tileX(z, p.x)
      val y = Tile.tileY(z, p.y)
      val tileName = s"$z-$x-$y"
      val tile = routeTileCache(tileName)
      foundTiles += tile.name -> tile
    }

    tileQueue ++= foundTiles.values

    // At this point we might be missing tiles between the segment endpoints, so for each
    // tile in the queue, the four adjacent tiles (left, right, top, bottom) are explored.
    while (tileQueue.nonEmpty) {
      val tile = tileQueue.dequeue()
      explore(tileQueue, foundTiles, lineSegments, tile, tile.bounds.leftLineSegment, -1, 0)
      explore(tileQueue, foundTiles, lineSegments, tile, tile.bounds.rightLineSegment, 1, 0)
      explore(tileQueue, foundTiles, lineSegments, tile, tile.bounds.topLineSegment, 0, 1)
      explore(tileQueue, foundTiles, lineSegments, tile, tile.bounds.bottomLineSegment, 0, -1)
    }
    foundTiles.values.toSeq
  }

  // Checks whether given line segment intersects with a side of the current tile
  // If it does, adds the adjacent tile to both the queue and the set of found tiles
  // Avoids re-processing tiles that have already been found
  private def explore(
    tileQueue: scala.collection.mutable.Queue[Tile],
    foundTiles: scala.collection.mutable.Map[String, Tile],
    lineSegments: Seq[LineSegment],
    tile: Tile,
    side: LineSegment,
    xDelta: Int,
    yDelta: Int
  ): Unit = {
    val x = tile.x + xDelta
    val y = tile.y + yDelta
    if (x >= 0 && y >= 0) {
      val tileName = s"${tile.z}-$x-$y"
      val adjecentTile = routeTileCache(tileName)
      if (!foundTiles.contains(adjecentTile.name)) {
        if (lineSegments.exists(_.intersection(side) != null)) {
          tileQueue += adjecentTile
          foundTiles += adjecentTile.name -> adjecentTile
        }
      }
    }
  }
}
