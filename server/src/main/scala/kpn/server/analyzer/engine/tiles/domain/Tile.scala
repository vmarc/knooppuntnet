package kpn.server.analyzer.engine.tiles.domain

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Polygon

object Tile {

  val TILE_SIZE = 256
  val EXTENT_STANDARD = TILE_SIZE
  val EXTENT_DETAILED = 4096

  val CLIP_BUFFER_SIZE_STANDARD = 14 // assume tile size 256 pixels, radius of node circle 14 pixels
  val CLIP_BUFFER_SIZE_DETAILED = EXTENT_DETAILED * 14 / TILE_SIZE

  val POI_CLIP_BUFFER_SIZE_STANDARD = 17 // assume tile size 256 pixels, radius of node circle 14 pixels
  val POI_CLIP_BUFFER_SIZE_DETAILED = EXTENT_DETAILED * 17 / TILE_SIZE

  val POI_CLIP_BUFFER: ClipBuffer = ClipBuffer(
    left = EXTENT_STANDARD * 17 / 256, // poi icon width 32 (32 / 2 + 1) -> 17
    right = EXTENT_STANDARD * 17 / 256,
    top = 0,
    bottom = EXTENT_STANDARD * 39 / 256 // poi icon height 37 (37 + 2)
  )

  def apply(z: Int, x: Int, y: Int): Tile = {
    new Tile(z, x, y)
  }

  def apply(tileId: TileId): Tile = {
    new Tile(tileId.z.toInt, tileId.x.toInt, tileId.y.toInt)
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

  val detailed = z >= 13

  val extent = if (detailed) {
    Tile.EXTENT_DETAILED
  }
  else {
    Tile.EXTENT_STANDARD
  }

  val poiDetailed = z >= 15

  val poiExtent = if (poiDetailed) {
    Tile.EXTENT_DETAILED
  }
  else {
    Tile.EXTENT_STANDARD
  }

  val clipBufferSize = if (z < 13) {
    Tile.CLIP_BUFFER_SIZE_STANDARD
  }
  else {
    Tile.CLIP_BUFFER_SIZE_DETAILED
  }

  val poiClipBufferSize = if (z < 15) {
    Tile.POI_CLIP_BUFFER_SIZE_STANDARD
  }
  else {
    Tile.POI_CLIP_BUFFER_SIZE_DETAILED
  }

  val tileEnvelope: Polygon = {
    val size = extent.toDouble
    val coords = new Array[Coordinate](5)
    coords(0) = new Coordinate(0d - clipBufferSize, size + clipBufferSize)
    coords(1) = new Coordinate(size + clipBufferSize, size + clipBufferSize)
    coords(2) = new Coordinate(size + clipBufferSize, 0d - clipBufferSize)
    coords(3) = new Coordinate(0d - clipBufferSize, 0d - clipBufferSize)
    coords(4) = coords(0)
    new GeometryFactory().createPolygon(coords)
  }

  val poiTileEnvelope: Polygon = {
    val size = extent.toDouble
    val coords = new Array[Coordinate](5)
    coords(0) = new Coordinate(0d - poiClipBufferSize, size + poiClipBufferSize)
    coords(1) = new Coordinate(size + poiClipBufferSize, size + poiClipBufferSize)
    coords(2) = new Coordinate(size + poiClipBufferSize, 0d - poiClipBufferSize)
    coords(3) = new Coordinate(0d - poiClipBufferSize, 0d - poiClipBufferSize)
    coords(4) = coords(0)
    new GeometryFactory().createPolygon(coords)
  }

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
    buildClipBounds()
  }

  val poiClipBounds: Rectangle = {
    buildClipBounds()
  }

  private def buildClipBounds(): Rectangle = {
    val xMin = worldXMin - ((worldXMax - worldXMin) * clipBufferSize / extent)
    val xMax = worldXMax + ((worldXMax - worldXMin) * clipBufferSize / extent)
    val yMin = worldYMin - ((worldYMax - worldYMin) * clipBufferSize / extent)
    val yMax = worldYMax + ((worldYMax - worldYMin) * clipBufferSize / extent)
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
    val scaledX = (worldCoordinate.x - worldXMin) * extent / (worldXMax - worldXMin)
    val scaledY = (worldCoordinate.y - worldYMin) * extent / (worldYMax - worldYMin)
    new Coordinate(scaledX, scaledY)
  }

  def poiScale(worldCoordinate: Coordinate): Coordinate = {
    val scaledX = (worldCoordinate.x - worldXMin) * poiExtent / (worldXMax - worldXMin)
    val scaledY = (worldCoordinate.y - worldYMin) * poiExtent / (worldYMax - worldYMin)
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

  //  def slowerContainsSaved(worldCoordinates: Seq[Double]): Boolean = {
  //    val xs = worldCoordinates.zipWithIndex.filter(_._2 % 2 == 0).map(_._1)
  //    val ys = worldCoordinates.zipWithIndex.filter(_._2 % 2 == 1).map(_._1)
  //    val xmin = xs.min
  //    val xmax = xs.max
  //    val ymin = ys.min
  //    val ymax = ys.max
  //    xmin < clipBounds.xMax &&
  //      clipBounds.xMin < xmax &&
  //      ymin < clipBounds.yMax &&
  //      clipBounds.yMin < ymax
  //  }

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
