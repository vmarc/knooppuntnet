package kpn.server.analyzer.engine.tiles.domain

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Polygon

object Tile {

  val TILE_SIZE = 256
  val EXTENT_STANDARD = TILE_SIZE
  val EXTENT_DETAILED = 4096

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

  def routeTileFromId(tileId: TileId): Tile = {
    routeTile(tileId.z.toInt, tileId.x.toInt, tileId.y.toInt)
  }

  def routeTile(z: Int, x: Int, y: Int): Tile = {
    val detailed = z >= 13
    val extent = if (detailed) {
      Tile.EXTENT_DETAILED
    }
    else {
      Tile.EXTENT_STANDARD
    }

    val clipBufferSize = if (z < 13) {
      14 // assume tile size 256 pixels, radius of node circle 14 pixels TODO redesign - could be smaller, because at these levels nodes do not have that size anymore?
    }
    else {
      Tile.EXTENT_DETAILED * 14 / Tile.TILE_SIZE
    }

    new Tile(z, x, y, detailed, extent, clipBufferSize)
  }

  def poiTileFromName(tilename: String): Tile = {
    val splitted = tilename.split("-")
    poiTile(splitted(0).toInt, splitted(1).toInt, splitted(2).toInt)
  }

  def poiTile(z: Int, x: Int, y: Int): Tile = {
    val detailed = z >= 15
    val extent = if (detailed) {
      Tile.EXTENT_DETAILED
    }
    else {
      Tile.EXTENT_STANDARD
    }

    val clipBufferSize = if (z < 15) {
      17 // assume tile size 256 pixels, shield height 17 pixels
    }
    else {
      EXTENT_DETAILED * 17 / TILE_SIZE
    }

    new Tile(z, x, y, detailed, extent, clipBufferSize)
  }
}

class Tile(
  val z: Int,
  val x: Int,
  val y: Int,
  val detailed: Boolean,
  val extent: Int,
  val clipBufferSize: Int
) {

  def id: TileId = {
    TileId(z, x, y)
  }

  def name: String = {
    s"$z-$x-$y"
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

  private def buildClipBounds(): Rectangle = {
    val xMin = worldXMin - ((worldXMax - worldXMin) * clipBufferSize / extent)
    val xMax = worldXMax + ((worldXMax - worldXMin) * clipBufferSize / extent)
    val yMin = worldYMin - ((worldYMax - worldYMin) * clipBufferSize / extent)
    val yMax = worldYMax + ((worldYMax - worldYMin) * clipBufferSize / extent)
    Rectangle(xMin, xMax, yMin, yMax)
  }

  override def toString: String = s"${this.getClass.getSimpleName}($name)"

  override def equals(obj: Any): Boolean = {
    obj.isInstanceOf[Tile] && obj.asInstanceOf[Tile].name == name
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
