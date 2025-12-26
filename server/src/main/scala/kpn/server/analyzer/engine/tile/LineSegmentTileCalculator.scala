package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.domain.Tile
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.LineSegment
import org.springframework.stereotype.Component

/*
  Calculates which map tiles are needed to display a set of line segments at a given zoom level.
 */
@Component
class LineSegmentTileCalculator(routeTileCache: RouteTileCache) {

  private case class Direction(xDelta: Int, yDelta: Int, getSide: Tile => LineSegment)

  private val directions = {
    val left = Direction(-1, 0, _.bounds.leftLineSegment)
    val right = Direction(1, 0, _.bounds.rightLineSegment)
    val top = Direction(0, -1, _.bounds.topLineSegment)
    val bottom = Direction(0, 1, _.bounds.bottomLineSegment)
    Seq(left, right, top, bottom)
  }

  def tiles(z: Int, lineSegments: Seq[LineSegment]): Seq[Tile] = {
    val initialTiles = findEndpointTiles(z, lineSegments)
    findItermediateTiles(initialTiles, lineSegments)
  }

  def tilesForLines(worldCoordinateReferenceLines: Seq[Seq[Coordinate]]): Seq[Tile] = {
    worldCoordinateReferenceLines.flatMap { worldCoordinates =>
      val lineSegments = worldCoordinates
        .sliding(2)
        .map { case Seq(c1, c2) => new LineSegment(c1, c2) }
        .toSeq
      (ZoomLevel.newMinZoom to ZoomLevel.newMaxZoom).flatMap { z =>
        tiles(z, lineSegments)
      }
    }.distinct
  }

  /**
   * Finds tiles containing the endpoints of all line segments
   */
  private def findEndpointTiles(z: Int, lineSegments: Seq[LineSegment]): Map[String, Tile] = {
    val endpoints = lineSegments.flatMap(ls => Seq(ls.p0, ls.p1))
    val tileNames = endpoints.map { point =>
      val x = Tile.tileX(z, point.x)
      val y = Tile.tileY(z, point.y)
      s"$z-$x-$y"
    }.distinct
    tileNames.map(tileName => tileName -> routeTileCache(tileName)).toMap
  }

  private def findItermediateTiles(initialTiles: Map[String, Tile], lineSegments: Seq[LineSegment]): Seq[Tile] = {

    val tileQueue = scala.collection.mutable.Queue[Tile]()
    var foundTiles = initialTiles

    tileQueue ++= foundTiles.values

    // At this point we might be missing tiles between the segment endpoints, so for each
    // tile in the queue, the four adjacent tiles (left, right, top, bottom) are explored.
    while (tileQueue.nonEmpty) {
      val currentTile = tileQueue.dequeue()
      val newTiles = directions.flatMap { direction =>
        exploreDirection(currentTile, lineSegments, direction, foundTiles)
      }
      // Update found tiles and queue with newly discovered tiles
      foundTiles ++= newTiles.map(tile => tile.name -> tile)
      tileQueue ++= newTiles
    }
    foundTiles.values.toSeq
  }

  /**
   * Explores in a specific direction from the current tile.
   * Returns the adjacent tile if it intersects with any line segment and hasn't been found yet.
   */
  private def exploreDirection(
    tile: Tile,
    lineSegments: Seq[LineSegment],
    direction: Direction,
    foundTiles: Map[String, Tile]
  ): Option[Tile] = {
    val x = tile.x + direction.xDelta
    val y = tile.y + direction.yDelta

    if (x < 0 || y < 0) {
      return None
    }

    val tileName = s"${tile.z}-$x-$y"
    val adjacentTile = routeTileCache(tileName)

    if (foundTiles.contains(adjacentTile.name)) {
      return None
    }

    val side = direction.getSide(tile)
    if (lineSegments.exists(_.intersection(side) != null)) {
      Some(adjacentTile)
    } else {
      None
    }
  }
}
