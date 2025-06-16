package kpn.server.analyzer.engine.tiles.domain

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Polygon

object TileContext {

  private val TileSize = 256
  private val StandardExtent = TileSize
  private val DetailedExtent = 4096
  private val RouteDetailZoomThreshold = 13 // zoomlevel at which DetailExtent is used
  private val PoiDetailZoomThreshold = 15

  private val RouteBufferSizePixelsLarge = 14
  private val RouteBufferSizePixelsSmall = 3
  private val PoiBufferSizePixels = 17

  private val geometryFactory = new GeometryFactory()

  def route(zoomLevel: Int): TileContext = {
    val detailed = zoomLevel >= RouteDetailZoomThreshold
    val clipBufferSize = if (zoomLevel < RouteDetailZoomThreshold) {
      RouteBufferSizePixelsSmall // assume tile size 256 pixels, radius of node circle 3/256 pixels
    }
    else {
      DetailedExtent * RouteBufferSizePixelsLarge / TileSize // detailed (radius of node circle 14/256 pixels)
    }
    apply(detailed: Boolean, clipBufferSize)
  }

  def poi(zoomLevel: Int): TileContext = {
    val detailed = zoomLevel >= PoiDetailZoomThreshold
    val clipBufferSize = if (zoomLevel < RouteDetailZoomThreshold) {
      PoiBufferSizePixels // assume tile size 256 pixels, radius of node circle 14 pixels
    }
    else {
      DetailedExtent * PoiBufferSizePixels / TileSize
    }
    apply(detailed: Boolean, clipBufferSize)
  }

  def apply(detailed: Boolean, clipBufferSize: Int): TileContext = {

    val extent = determineExtent(detailed)
    val tileEnvelope = calculateTileEnvelope(extent, clipBufferSize)

    TileContext(
      detailed,
      extent,
      clipBufferSize,
      tileEnvelope
    )
  }

  private def determineExtent(detailed: Boolean): Int = {
    if (detailed) DetailedExtent else StandardExtent
  }

  private def calculateTileEnvelope(extent: Int, clipBufferSize: Int): Polygon = {
    val size = extent.toDouble

    val xLeft = 0d - clipBufferSize
    val xRight = size + clipBufferSize
    val yTop = 0d - clipBufferSize
    val yBottom = size + clipBufferSize

    val bottomLeft = new Coordinate(xLeft, yBottom)
    val bottomRight = new Coordinate(xRight, yBottom)
    val topRight = new Coordinate(xRight, yTop)
    val topLeft = new Coordinate(xLeft, yTop)

    val coords = Array[Coordinate](
      bottomLeft,
      bottomRight,
      topRight,
      topLeft,
      bottomLeft,
    )
    geometryFactory.createPolygon(coords)
  }
}

case class TileContext(
  detailed: Boolean,
  extent: Int,
  clipBufferSize: Int,
  tileEnvelope: Polygon, // in tile coordinates
) {

  def toTileCoordinate(tile: Tile, worldCoordinates: Seq[Coordinate]): Seq[Coordinate] = {
    worldCoordinates.map(c => toTileCoordinate(tile, c))
  }

  def toTileCoordinate(tile: Tile, worldCoordinate: Coordinate): Coordinate = {
    val scaledX = toTileCoordinate(worldCoordinate.x, tile.bounds.xMin, tile.bounds.width)
    val scaledY = toTileCoordinate(worldCoordinate.y, tile.bounds.yMin, tile.bounds.height)
    new Coordinate(scaledX, scaledY)
  }

  private def toTileCoordinate(value: Double, min: Double, dimension: Double): Double = {
    (value - min) * extent / dimension
  }

  def tile(tileId: TileId): Tile = {

    val zoomFactor = calculateZoomFactor(tileId.z)

    val worldXMin = tileId.x.toDouble / zoomFactor
    val worldXMax = (tileId.x.toDouble + 1) / zoomFactor
    val worldYMin = tileId.y.toDouble / zoomFactor
    val worldYMax = (tileId.y.toDouble + 1) / zoomFactor

    val bounds = Rectangle(worldXMin, worldXMax, worldYMin, worldYMax)

    val clipBounds = calculateClipBounds(extent, clipBufferSize, worldXMin, worldXMax, worldYMin, worldYMax)

    new Tile(
      tileId,
      bounds,
      clipBounds
    )
  }

  // the number of tiles across the map in each direction
  private def calculateZoomFactor(z: Int): Int = {
    1 << z
  }

  private def calculateClipBounds(extent: Int, clipBufferSize: Int, worldXMin: Double, worldXMax: Double, worldYMin: Double, worldYMax: Double): Rectangle = {
    val xMin = worldXMin - ((worldXMax - worldXMin) * clipBufferSize / extent)
    val xMax = worldXMax + ((worldXMax - worldXMin) * clipBufferSize / extent)
    val yMin = worldYMin - ((worldYMax - worldYMin) * clipBufferSize / extent)
    val yMax = worldYMax + ((worldYMax - worldYMin) * clipBufferSize / extent)
    Rectangle(xMin, xMax, yMin, yMax)
  }
}
