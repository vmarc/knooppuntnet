package kpn.server.analyzer.engine.tiles.domain

import org.locationtech.jts.geom.Coordinate

object Tile {

  val EXTENT: Int = 256

  val CLIP_BUFFER: ClipBuffer = ClipBuffer(
    EXTENT * 14 / 256, // assume tile size 256 pixels, radius of node circle 14 pixels
    EXTENT * 14 / 256,
    EXTENT * 14 / 256,
    EXTENT * 14 / 256
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

  val clipBounds: Rectangle = {
    buildClipBounds(OldTile.CLIP_BUFFER)
  }

  val poiClipBounds: Rectangle = {
    buildClipBounds(OldTile.POI_CLIP_BUFFER)
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

  def scale(coordinate: Coordinate): Coordinate = {
    val scaledX = ((coordinate.x - worldXMin) * 256 / (worldXMax - worldXMin))
    val scaledY = ((coordinate.y - worldYMin) * 256 / (worldYMax - worldYMin))
    new Coordinate(scaledX, scaledY)
  }
}
