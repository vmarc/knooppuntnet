package kpn.server.analyzer.engine.tiles.domain

import kpn.api.common.LatLon
import kpn.server.analyzer.engine.tile.NodeTileCalculator
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.latToWorldY
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.lonToWorldX

/*
  Calculates which map tiles are needed to display a node at a given zoom level.
  When the node is near the edge of a map tile (clip bounds), it needs to be included
  in multiple tiles to ensure proper rendering.
 */
object PointTileCalculator {
  private case class Direction(xOffset: Int, yOffset: Int)

  private val Center = Direction(0, 0)
  private val West = Direction(-1, 0)
  private val East = Direction(1, 0)
  private val North = Direction(0, -1)
  private val South = Direction(0, 1)
  private val NorthWest = Direction(-1, -1)
  private val NorthEast = Direction(1, -1)
  private val SouthWest = Direction(-1, 1)
  private val SouthEast = Direction(1, 1)

  private val directions = Seq(
    Center, West, East, NorthWest, North, NorthEast, SouthWest, South, SouthEast
  )
}

class PointTileCalculator(protected val tileCache: TileCache) extends NodeTileCalculator {

  override def tiles(z: Int, latLon: LatLon): Seq[Tile] = {

    val xWorld = lonToWorldX(latLon.lon)
    val yWorld = latToWorldY(latLon.lat)

    val x = Tile.tileX(z, xWorld)
    val y = Tile.tileY(z, yWorld)

    PointTileCalculator.directions.flatMap { direction =>
      explore(xWorld, yWorld, z, x + direction.xOffset, y + direction.yOffset)
    }
  }

  private def explore(xWorld: Double, yWorld: Double, z: Int, x: Int, y: Int): Option[Tile] = {
    val tileName = s"$z-$x-$y"
    val tile = tileCache(tileName)
    Option.when(tile.clipBounds.contains(xWorld, yWorld)) {
      tile
    }
  }
}
