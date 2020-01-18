package kpn.server.analyzer.engine.elevation

import kpn.core.common.LatLonD
import kpn.server.analyzer.engine.elevation.ElevationTile.elevationValuesPerLine

object ElevationTile {

  private val secondsPerMinute = 60
  private val resolutionInArcSeconds = 3
  private val elevationValuesPerLine = 1201

  def apply(latLon: LatLonD): ElevationTile = {
    val row: Int = elevationValuesPerLine - Math.round(fractionalPart(latLon.lat) * secondsPerMinute * secondsPerMinute / resolutionInArcSeconds).toInt
    val column: Int = Math.round(fractionalPart(latLon.lon) * secondsPerMinute * secondsPerMinute / resolutionInArcSeconds).toInt
    ElevationTile(latLon.lat.toInt, latLon.lon.toInt, row, column)
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

  private def latMin: Double = 0 // TODO

  private def latMax: Double = 0 // TODO

  private def lonMin: Double = 0 // TODO

  private def lonMax: Double = 0 // TODO

  def top: LatLonLine = LatLonLine(LatLonD(latMin, lonMin), LatLonD(latMax, latMin))

  def bottom: LatLonLine = LatLonLine(LatLonD(latMin, lonMax), LatLonD(latMax, latMax))

  def left: LatLonLine = LatLonLine(LatLonD(latMin, lonMin), LatLonD(latMin, latMax))

  def right: LatLonLine = LatLonLine(LatLonD(latMax, lonMin), LatLonD(latMax, latMax))

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
