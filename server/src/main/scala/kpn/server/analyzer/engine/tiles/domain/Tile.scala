package kpn.server.analyzer.engine.tiles.domain

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

  def contains(worldCoordinates: Seq[Double]): Boolean = {
    var xmin = worldCoordinates.head
    var xmax = xmin
    var ymin = worldCoordinates(1)
    var ymax = ymin
    worldCoordinates.sliding(2, 2).foreach { case Seq(x, y) =>
      if (x < xmin) xmin = x
      if (x > xmax) xmax = x
      if (y < ymin) ymin = y
      if (y > ymax) ymax = y
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

  private def boundsOverlapClipBounds(xmin: Double, xmax: Double, ymin: Double, ymax: Double): Boolean = {
    xmin < clipBounds.xMax &&
      clipBounds.xMin < xmax &&
      ymin < clipBounds.yMax &&
      clipBounds.yMin < ymax
  }
}
