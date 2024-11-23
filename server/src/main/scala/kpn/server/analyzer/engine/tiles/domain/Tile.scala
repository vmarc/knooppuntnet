package kpn.server.analyzer.engine.tiles.domain

import org.locationtech.jts.geom.Coordinate

object Tile {

  val EXTENT: Int = 4096

  val CLIP_BUFFER_SIZE = 14 // assume tile size 256 pixels, radius of node circle 14 pixels

  val CLIP_BUFFER: ClipBuffer = ClipBuffer(
    EXTENT * CLIP_BUFFER_SIZE / 256,
    EXTENT * CLIP_BUFFER_SIZE / 256,
    EXTENT * CLIP_BUFFER_SIZE / 256,
    EXTENT * CLIP_BUFFER_SIZE / 256
  )

  val POI_CLIP_BUFFER: ClipBuffer = ClipBuffer(
    left = EXTENT * 17 / 256, // poi icon width 32 (32 / 2 + 1) -> 17
    right = EXTENT * 17 / 256,
    top = 0,
    bottom = EXTENT * 39 / 256 // poi icon height 37 (37 + 2)
  )

  def apply(z: Int, x: Int, y: Int): Tile = {
    new Tile(z, x, y)
  }

  // x part of the z-x-y tilename
  def tileX(z: Int, worldX: Double): Int = {
    val zoomFactor = 1 << z // the number of tiles across the map in each direction
    (worldX * zoomFactor).toInt
  }

  // y part of the z-x-y tilename
  def tileY(z: Int, worldY: Double): Int = {
    val zoomFactor = 1 << z // the number of tiles across the map in each direction
    (worldY * zoomFactor).toInt
  }
}

class Tile(val z: Int, val x: Int, val y: Int) {

  val name: String = s"$z-$x-$y"

  val zoomFactor = 1 << z // the number of tiles across the map in each direction
  val worldXMin = x.toDouble / zoomFactor
  val worldXMax = (x.toDouble + 1) / zoomFactor
  val worldYMin = y.toDouble / zoomFactor
  val worldYMax = (y.toDouble + 1) / zoomFactor

  val bounds: Rectangle = {
    Rectangle(
      worldXMin,
      worldXMax,
      worldYMin,
      worldYMax
    )
  }

  val clipBounds: Rectangle = {
    buildClipBounds(Tile.CLIP_BUFFER)
  }

  val poiClipBounds: Rectangle = {
    buildClipBounds(Tile.POI_CLIP_BUFFER)
  }

  private def buildClipBounds(clipBuffer: ClipBuffer): Rectangle = {
    val xMin = worldXMin - ((worldXMax - worldXMin) * clipBuffer.left / Tile.EXTENT)
    val xMax = worldXMax + ((worldXMax - worldXMin) * clipBuffer.right / Tile.EXTENT)
    val yMin = worldYMin - ((worldYMax - worldYMin) * clipBuffer.bottom / Tile.EXTENT)
    val yMax = worldYMax + ((worldYMax - worldYMin) * clipBuffer.top / Tile.EXTENT)
    Rectangle(xMin, xMax, yMin, yMax)
  }

  override def toString: String = s"${this.getClass.getSimpleName}($name)"

  override def equals(obj: Any): Boolean = {
    obj.isInstanceOf[OldTile] && obj.asInstanceOf[OldTile].name == name
  }

  override def hashCode(): Int = name.hashCode()

  def scale(worldCoordinates: Seq[Coordinate]): Seq[Coordinate] = {
    worldCoordinates.map(scale)
  }

  def scale(worldCoordinate: Coordinate): Coordinate = {
    //    if (worldCoordinate.x < 0 || worldCoordinate.x > 1) {
    //      throw new RuntimeException(s"invalid worldCoordinate x: ${worldCoordinate.x} (should be between 0 and 1)")
    //    }
    //    if (worldCoordinate.y < 0 || worldCoordinate.y > 1) {
    //      throw new RuntimeException(s"invalid worldCoordinate y: ${worldCoordinate.y} (should be between 0 and 1)")
    //    }
    //
    //    if (worldCoordinate.x < worldXMin || worldCoordinate.x > worldXMax) {
    //      throw new RuntimeException(s"invalid worldCoordinate x: ${worldCoordinate.x} (should be between $worldXMin and $worldXMax)")
    //    }
    //    if (worldCoordinate.y < worldYMin || worldCoordinate.y > worldYMax) {
    //      throw new RuntimeException(s"invalid worldCoordinate y: ${worldCoordinate.y} (should be between $worldYMin and $worldYMax)")
    //    }

    val scaledX = (worldCoordinate.x - worldXMin) * Tile.EXTENT / (worldXMax - worldXMin)
    val scaledY = (worldCoordinate.y - worldYMin) * Tile.EXTENT / (worldYMax - worldYMin)

    //    if (scaledX < 0 || scaledX > Tile.EXTENT) {
    //      throw new RuntimeException(s"invalid scaledCoordinate x: $scaledX (should be between 0 and ${Tile.EXTENT})")
    //    }
    //    if (scaledY < 0 || scaledY > Tile.EXTENT) {
    //      throw new RuntimeException(s"invalid scaledCoordinate y: $scaledY (should be between 0 and ${Tile.EXTENT})")
    //    }

    new Coordinate(scaledX, scaledY)
  }

  def scale14(worldCoordinate: Coordinate): Coordinate = {
    //    if (worldCoordinate.x < 0 || worldCoordinate.x > 1) {
    //      throw new RuntimeException(s"invalid worldCoordinate x: ${worldCoordinate.x} (should be between 0 and 1)")
    //    }
    //    if (worldCoordinate.y < 0 || worldCoordinate.y > 1) {
    //      throw new RuntimeException(s"invalid worldCoordinate y: ${worldCoordinate.y} (should be between 0 and 1)")
    //    }
    //
    //    if (worldCoordinate.x < worldXMin || worldCoordinate.x > worldXMax) {
    //      throw new RuntimeException(s"invalid worldCoordinate x: ${worldCoordinate.x} (should be between $worldXMin and $worldXMax)")
    //    }
    //    if (worldCoordinate.y < worldYMin || worldCoordinate.y > worldYMax) {
    //      throw new RuntimeException(s"invalid worldCoordinate y: ${worldCoordinate.y} (should be between $worldYMin and $worldYMax)")
    //    }

    val scaledX = (worldCoordinate.x - worldXMin) * 4096 / (worldXMax - worldXMin)
    val scaledY = (worldCoordinate.y - worldYMin) * 4096 / (worldYMax - worldYMin)

    //    if (scaledX < 0 || scaledX > 4096) {
    //      throw new RuntimeException(s"invalid scaledCoordinate x: $scaledX (should be between 0 and ${4096})")
    //    }
    //    if (scaledY < 0 || scaledY > 4096) {
    //      throw new RuntimeException(s"invalid scaledCoordinate y: $scaledY (should be between 0 and ${4096})")
    //    }

    new Coordinate(scaledX, scaledY)
  }

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
    xmin < clipBounds.xMax &&
      clipBounds.xMin < xmax &&
      ymin < clipBounds.yMax &&
      clipBounds.yMin < ymax
  }

  def slowerContainsSaved(worldCoordinates: Seq[Double]): Boolean = {
    val xs = worldCoordinates.zipWithIndex.filter(_._2 % 2 == 0).map(_._1)
    val ys = worldCoordinates.zipWithIndex.filter(_._2 % 2 == 1).map(_._1)
    val xmin = xs.min
    val xmax = xs.max
    val ymin = ys.min
    val ymax = ys.max
    xmin < clipBounds.xMax &&
      clipBounds.xMin < xmax &&
      ymin < clipBounds.yMax &&
      clipBounds.yMin < ymax
  }

  def containsLine(x1: Double, y1: Double, x2: Double, y2: Double): Boolean = {
    val xmin = if (x1 < x2) x1 else x2
    val xmax = if (x1 > x2) x1 else x2
    val ymin = if (y1 < y2) y1 else y2
    val ymax = if (y1 > y2) y1 else y2
    xmin < clipBounds.xMax &&
      clipBounds.xMin < xmax &&
      ymin < clipBounds.yMax &&
      clipBounds.yMin < ymax
  }
}
