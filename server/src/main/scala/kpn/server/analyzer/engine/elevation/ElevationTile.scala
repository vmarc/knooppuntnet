package kpn.server.analyzer.engine.elevation

import kpn.core.common.LatLonD
import kpn.server.analyzer.engine.elevation.ElevationTile.elevationValuesPerLine

object ElevationTile {

  private val elevationValuesPerLine = 1201

  def apply(latLon: LatLonD): ElevationTile = {
    val row: Int = elevationValuesPerLine - Math.round(fractionalPart(latLon.lat) * 1200).toInt
    val column: Int = Math.round(fractionalPart(latLon.lon) * 1200).toInt
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

  private def latMin: Double = lat.toDouble

  private def latMax: Double = (lat + 1).toDouble

  private def lonMin: Double = lon.toDouble

  private def lonMax: Double = (lon + 1).toDouble

  def top: LatLonLine = LatLonLine(LatLonD(latMin, lonMin), LatLonD(latMax, lonMin))

  def bottom: LatLonLine = LatLonLine(LatLonD(latMin, lonMax), LatLonD(latMax, lonMax))

  def left: LatLonLine = LatLonLine(LatLonD(latMin, lonMin), LatLonD(latMin, lonMax))

  def right: LatLonLine = LatLonLine(LatLonD(latMax, lonMin), LatLonD(latMax, lonMax))

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
