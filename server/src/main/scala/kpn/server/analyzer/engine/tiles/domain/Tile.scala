package kpn.server.analyzer.engine.tiles.domain

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Polygon

object Tile {

  private val TILE_SIZE: Int = 256
  private val EXTENT_STANDARD: Int = TILE_SIZE
  private val EXTENT_DETAILED: Int = 4096

  // Constants for zoom level thresholds
  private val ROUTE_DETAIL_ZOOM_THRESHOLD: Int = 13
  private val POI_DETAIL_ZOOM_THRESHOLD: Int = 15

  // Constants for buffer sizes
  private val ROUTE_BUFFER_SIZE_PIXELS: Int = 14
  private val POI_BUFFER_SIZE_PIXELS: Int = 17

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

  def routeTileFromId(tileId: TileId): Tile = {

    val detailed = tileId.z >= ROUTE_DETAIL_ZOOM_THRESHOLD

    val extent = if (detailed) {
      EXTENT_DETAILED
    }
    else {
      EXTENT_STANDARD
    }

    val clipBufferSize = if (tileId.z < ROUTE_DETAIL_ZOOM_THRESHOLD) {
      ROUTE_BUFFER_SIZE_PIXELS // assume tile size 256 pixels, radius of node circle 14 pixels TODO redesign - could be smaller, because at these levels nodes do not have that size anymore?
    }
    else {
      Tile.EXTENT_DETAILED * ROUTE_BUFFER_SIZE_PIXELS / Tile.TILE_SIZE
    }

    buildTile(tileId, detailed, extent, clipBufferSize)
  }

  def routeTile(z: Int, x: Int, y: Int): Tile = {
    routeTileFromId(TileId(z, x, y))
  }

  def routeTileFromName(tilename: String): Tile = {
    val splitted = tilename.split("-")
    routeTile(splitted(0).toInt, splitted(1).toInt, splitted(2).toInt)
  }

  def poiTileFromName(tilename: String): Tile = {
    val splitted = tilename.split("-")
    poiTile(splitted(0).toInt, splitted(1).toInt, splitted(2).toInt)
  }

  def poiTile(z: Int, x: Int, y: Int): Tile = {
    val detailed = z >= POI_DETAIL_ZOOM_THRESHOLD
    val extent = if (detailed) {
      EXTENT_DETAILED
    }
    else {
      EXTENT_STANDARD
    }

    val clipBufferSize = if (z < POI_DETAIL_ZOOM_THRESHOLD) {
      POI_BUFFER_SIZE_PIXELS // assume tile size 256 pixels, shield height 17 pixels
    }
    else {
      EXTENT_DETAILED * POI_BUFFER_SIZE_PIXELS / TILE_SIZE
    }
    val tileId = TileId(z, x, y)
    buildTile(tileId, detailed, extent, clipBufferSize)
  }

  private def buildTile(
    tileId: TileId,
    detailed: Boolean,
    extent: Int,
    clipBufferSize: Int
  ): Tile = {

    val tileEnvelope = calculateTileEnvelope(extent, clipBufferSize)
    val zoomFactor = calculateZoomFactor(tileId.z)

    val worldXMin = tileId.x.toDouble / zoomFactor
    val worldXMax = (tileId.x.toDouble + 1) / zoomFactor
    val worldYMin = tileId.y.toDouble / zoomFactor
    val worldYMax = (tileId.y.toDouble + 1) / zoomFactor

    val bounds = Rectangle(worldXMin, worldXMax, worldYMin, worldYMax)

    val clipBounds = calculateClipBounds(extent, clipBufferSize, worldXMin, worldXMax, worldYMin, worldYMax)

    new Tile(
      tileId,
      detailed,
      extent,
      clipBufferSize,
      tileEnvelope,
      bounds,
      clipBounds
    )
  }

  private def calculateClipBounds(extent: Int, clipBufferSize: Int, worldXMin: Double, worldXMax: Double, worldYMin: Double, worldYMax: Double): Rectangle = {
    val xMin = worldXMin - ((worldXMax - worldXMin) * clipBufferSize / extent)
    val xMax = worldXMax + ((worldXMax - worldXMin) * clipBufferSize / extent)
    val yMin = worldYMin - ((worldYMax - worldYMin) * clipBufferSize / extent)
    val yMax = worldYMax + ((worldYMax - worldYMin) * clipBufferSize / extent)
    Rectangle(xMin, xMax, yMin, yMax)
  }

  private def calculateTileEnvelope(extent: Int, clipBufferSize: Int): Polygon = {
    val size = extent.toDouble
    val coords = new Array[Coordinate](5)
    coords(0) = new Coordinate(0d - clipBufferSize, size + clipBufferSize)
    coords(1) = new Coordinate(size + clipBufferSize, size + clipBufferSize)
    coords(2) = new Coordinate(size + clipBufferSize, 0d - clipBufferSize)
    coords(3) = new Coordinate(0d - clipBufferSize, 0d - clipBufferSize)
    coords(4) = coords(0)
    new GeometryFactory().createPolygon(coords)
  }
}

case class Tile(
  id: TileId,
  detailed: Boolean, // TODO move to TileContext (new class)
  extent: Int, // TODO move to TileContext
  clipBufferSize: Int, // TODO move to TileContext
  tileEnvelope: Polygon, // TODO move to TileContext
  bounds: Rectangle, // bounds in world coordinates
  clipBounds: Rectangle, // clip bounds in world coordinates (= bounds + small offset)
) {

  def z: Int = id.z

  def x: Int = id.x

  def y: Int = id.y

  def name: String = id.name

  def scale(worldCoordinates: Seq[Coordinate]): Seq[Coordinate] = {
    worldCoordinates.map(scale)
  }

  def scale(worldCoordinate: Coordinate): Coordinate = {
    val scaledX = (worldCoordinate.x - bounds.xMin) * extent / bounds.width
    val scaledY = (worldCoordinate.y - bounds.yMin) * extent / bounds.height

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
