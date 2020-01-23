package kpn.server.analyzer.engine.elevation

import kpn.server.analyzer.engine.elevation.ElevationTile.elevationValuesPerLine
import kpn.server.analyzer.engine.tiles.domain.Point
import kpn.server.analyzer.engine.tiles.domain.Rectangle

object ElevationTile {

  private val elevationValuesPerLine = 1201

  def apply(point: Point): ElevationTile = {
    val column: Int = Math.round(fractionalPart(point.x) * 1200).toInt
    val row: Int = elevationValuesPerLine - Math.round(fractionalPart(point.y) * 1200).toInt
    ElevationTile(point.x.toInt, point.y.toInt, column, row)
  }

  private def fractionalPart(value: Double): Double = {
    val integerPart = value.toLong
    value - integerPart
  }

}

case class ElevationTile(
  left: Int, // X lon
  bottom: Int, // Y lat
  column: Int, // X 0 based column index
  row: Int // Y 0 based row index
) {

  def bufferIndex: Int = (elevationValuesPerLine * row) + column

  def name: String = {
    val latPref = if (bottom < 0) "S" else "N"
    val lonPref = if (left < 0) "W" else "E"
    "%s%02d%s%03d".format(latPref, bottom, lonPref, left)
  }

  def fullName: String = {
    s"$name $row $column"
  }

  private def xMin: Double = left.toDouble

  private def xMax: Double = xMin + (column.toDouble / 1200)

  private def yMin: Double = bottom.toDouble

  private def yMax: Double = yMin + (row.toDouble / 1200)

  def bounds: Rectangle = {
    Rectangle(xMin, xMax, yMin, yMax)
  }

  def adjecent(columnDelta: Int, rowDelta: Int): ElevationTile = {

    var newLeft = left
    var newBottom = bottom

    var newRow = row + rowDelta
    if (newRow < 0) {
      newLeft = left - 1
      newRow = elevationValuesPerLine - 1
    }
    else if (newRow >= elevationValuesPerLine) {
      newLeft = left + 1
      newRow = 0 // TODO or  1 ?
    }

    var newColumn = column + columnDelta
    if (newColumn < 0) {
      newBottom = bottom - 1
      newColumn = elevationValuesPerLine - 1
    }
    else if (newColumn >= elevationValuesPerLine) {
      newBottom = bottom + 1
      newColumn = 0 // TODO or  1 ?
    }

    ElevationTile(newLeft, newBottom, newRow, newColumn)
  }

}
