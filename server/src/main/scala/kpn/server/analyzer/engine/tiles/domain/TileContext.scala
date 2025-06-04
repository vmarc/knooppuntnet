package kpn.server.analyzer.engine.tiles.domain

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Polygon

object TileContext {

  private val TileSize = 256
  private val StandardExtent = TileSize
  private val DetailedExtent = 4096
  private val RouteDetailZoomThreshold = 13
  private val PoiDetailZoomThreshold = 15

  private val RouteBufferSizePixels = 14
  private val PoiBufferSizePixels = 17

  private val geometryFactory = new GeometryFactory()

  def route(zoomLevel: Int): TileContext = {
    val detailed = zoomLevel >= RouteDetailZoomThreshold
    val clipBufferSize = if (zoomLevel < RouteDetailZoomThreshold) {
      RouteBufferSizePixels // assume tile size 256 pixels, radius of node circle 14 pixels TODO redesign - could be smaller, because at these levels nodes do not have that size anymore?
    }
    else {
      DetailedExtent * RouteBufferSizePixels / TileSize
    }
    apply(detailed: Boolean, clipBufferSize)
  }

  def poi(zoomLevel: Int): TileContext = {
    val detailed = zoomLevel >= PoiDetailZoomThreshold
    val clipBufferSize = if (zoomLevel < RouteDetailZoomThreshold) {
      PoiBufferSizePixels // assume tile size 256 pixels, radius of node circle 14 pixels TODO redesign - could be smaller, because at these levels nodes do not have that size anymore?
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
    val xRight = 0d - clipBufferSize
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
    worldCoordinates.map(c => toTileCoorinate(tile, c))
  }

  def toTileCoorinate(tile: Tile, worldCoordinate: Coordinate): Coordinate = {
    val scaledX = scaleCoordinate(worldCoordinate.x, tile.bounds.xMin, tile.bounds.width)
    val scaledY = scaleCoordinate(worldCoordinate.y, tile.bounds.yMin, tile.bounds.height)
    new Coordinate(scaledX, scaledY)
  }

  private def scaleCoordinate(value: Double, min: Double, dimension: Double): Double = {
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
