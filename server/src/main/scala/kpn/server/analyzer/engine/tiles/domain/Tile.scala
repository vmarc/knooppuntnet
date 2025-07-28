package kpn.server.analyzer.engine.tiles.domain

import org.locationtech.jts.geom.Coordinate

object Tile {

  // x part of the z-x-y tilename
  def tileX(z: Int, worldX: Double): Int = {
    val zoomFactor = calculateZoomFactor(z)
    (worldX * zoomFactor).toInt
  }

  // y part of the z-x-y tilename
  def tileY(z: Int, worldY: Double): Int = {
    val zoomFactor = calculateZoomFactor(z)
    (worldY * zoomFactor).toInt
  }

  // the number of tiles across the map in each direction
  private def calculateZoomFactor(z: Int): Int = {
    1 << z
  }
}

case class Tile(
  id: TileId,
  bounds: Rectangle, // bounds in world coordinates
  clipBounds: Rectangle, // clip bounds in world coordinates (= bounds + small offset)
) {

  def z: Int = id.z

  def x: Int = id.x

  def y: Int = id.y

  def name: String = id.name

  def contains(worldCoordinates: Seq[Coordinate]): Boolean = {
    if (worldCoordinates.isEmpty) {
      return false
    }
    var xmin = worldCoordinates.head.x
    var xmax = xmin
    var ymin = worldCoordinates.head.y
    var ymax = ymin
    worldCoordinates.tail.foreach { c =>
      if (c.x < xmin) xmin = c.x
      if (c.x > xmax) xmax = c.x
      if (c.y < ymin) ymin = c.y
      if (c.y > ymax) ymax = c.y
    }
    boundsOverlapClipBounds(xmin, xmax, ymin, ymax)
  }

  def containsLine(x1: Double, y1: Double, x2: Double, y2: Double): Boolean = {
    val xmin = math.min(x1, x2)
    val xmax = math.max(x1, x2)
    val ymin = math.min(y1, y2)
    val ymax = math.max(y1, y2)
    boundsOverlapClipBounds(xmin, xmax, ymin, ymax)
  }

  def boundsOverlapClipBounds(xmin: Double, xmax: Double, ymin: Double, ymax: Double): Boolean = {
    xmin < clipBounds.xMax &&
      clipBounds.xMin < xmax &&
      ymin < clipBounds.yMax &&
      clipBounds.yMin < ymax
  }
}
