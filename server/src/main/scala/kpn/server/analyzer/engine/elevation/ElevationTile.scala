package kpn.server.analyzer.engine.elevation

import kpn.server.analyzer.engine.elevation.ElevationTile.elevationValuesPerLine
import kpn.server.analyzer.engine.tiles.domain.Line
import kpn.server.analyzer.engine.tiles.domain.Point

object ElevationTile {

  private val elevationValuesPerLine = 1201

  def apply(point: Point): ElevationTile = {
    val row: Int = elevationValuesPerLine - Math.round(fractionalPart(point.x) * 1200).toInt
    val column: Int = Math.round(fractionalPart(point.y) * 1200).toInt
    ElevationTile(point.x.toInt, point.y.toInt, row, column)
  }

  private def fractionalPart(value: Double): Double = {
    val integerPart = value.toLong
    value - integerPart
  }

}

case class ElevationTile(lat: Int, lon: Int, row: Int, column: Int) {

  def bufferIndex: Int = (elevationValuesPerLine * (row - 1)) + column

  def name: String = {
    val latPref = if (lat < 0) "S" else "N"
    val lonPref = if (lon < 0) "W" else "E"
    "%s%02d%s%03d".format(latPref, lat, lonPref, lon)
  }

  private def latMin: Double = lat.toDouble

  private def latMax: Double = (lat + 1).toDouble

  private def lonMin: Double = lon.toDouble

  private def lonMax: Double = (lon + 1).toDouble

  def top: Line = Line(Point(latMin, lonMin), Point(latMax, lonMin))

  def bottom: Line = Line(Point(latMin, lonMax), Point(latMax, lonMax))

  def left: Line = Line(Point(latMin, lonMin), Point(latMin, lonMax))

  def right: Line = Line(Point(latMax, lonMin), Point(latMax, lonMax))

  def adjecent(rowDelta: Int, columnDelta: Int): ElevationTile = {

    var newLat = lat
    var newLon = lon

    var newRow = row + rowDelta
    if (newRow < 0) {
      newLat = lat - 1
      newRow = elevationValuesPerLine - 1
    }
    else if (newRow >= elevationValuesPerLine) {
      newLat = lat + 1
      newRow = 0 // TODO or  1 ?
    }

    var newColumn = column + columnDelta
    if (newColumn < 0) {
      newLon = lon - 1
      newColumn = elevationValuesPerLine - 1
    }
    else if (newColumn >= elevationValuesPerLine) {
      newLon = lon + 1
      newColumn = 0 // TODO or  1 ?
    }

    ElevationTile(newLat, newLon, newRow, newColumn)
  }

}
