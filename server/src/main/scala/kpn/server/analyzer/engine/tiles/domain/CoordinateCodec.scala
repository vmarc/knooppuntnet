package kpn.server.analyzer.engine.tiles.domain

import org.locationtech.jts.geom.Coordinate

/**
 * Utility for encoding and decoding arrays of geographic coordinates to and from compact
 * string representations. Uses delta encoding to reduce size by storing differences between
 * consecutive coordinates.
 */
object CoordinateCodec {

  private val CoordinatePrecision = 10000000 // 7 decimal places precision

  case class StringCoordinate(x: String, y: String)

  case class LongCoordinate(x: Long, y: Long)

  /**
   * Encodes an array of coordinates into a compact string representation using delta encoding.
   *
   * @param coordinates The array of coordinates to encode
   * @return A string representation of the encoded coordinates
   */
  def encode(coordinates: Array[Coordinate]): String = {
    if (coordinates.isEmpty) {
      return "[]"
    }
    val longCoordinates = coordinates.map(toLongCoordinate)
    val encoded = toStringCoordinate(coordinates.head) +: deltaEncode(longCoordinates)
    format(encoded)
  }

  /**
   * Decodes a string representation back into an array of coordinates.
   *
   * @param coordinateString The string to decode
   * @return An array of coordinates
   */
  def decode(coordinateString: String): Array[Coordinate] = {
    val stringCoordinates = parseCoordinates(coordinateString)
    if (stringCoordinates.isEmpty) {
      return Array.empty
    }
    val headCoordinate = stringCoordinateToCoordinate(stringCoordinates.head)
    val headLongCoordinate = toLongCoordinate(headCoordinate)
    val longCoordinates = headLongCoordinate +: stringCoordinates.tail.map(stringCoordinateToLongCoordinate)
    val decoded = deltaDecode(longCoordinates)
    headCoordinate +: decoded.tail.map(toCoordinate)
  }

  /**
   * Encodes coordinates using delta encoding to reduce size.
   * Stores the first coordinate as-is, then for subsequent coordinates
   * stores the difference from the previous coordinate.
   */
  private def deltaEncode(coordinates: Array[LongCoordinate]): Array[StringCoordinate] = {
    if (coordinates.isEmpty) {
      return Array.empty
    }
    coordinates.zip(coordinates.drop(1)).map {
      case (prev, curr) => StringCoordinate(
        (curr.x - prev.x).toString,
        (curr.y - prev.y).toString
      )
    }
  }

  private def parseCoordinates(coordinateString: String): Array[StringCoordinate] = {
    if (coordinateString.isEmpty) {
      return Array.empty
    }
    val stripped = coordinateString.stripPrefix("[").stripSuffix("]")
    if (stripped.isEmpty) {
      return Array.empty
    }
    stripped.split("],\\[").map { pair =>
      val coordParts = pair.replaceAll("[\\[\\]]", "").split(",")
      if (coordParts.length < 2) {
        throw new IllegalArgumentException(s"Invalid coordinate string format: $pair")
      }
      StringCoordinate(coordParts(0), coordParts(1))
    }
  }

  private def deltaDecode(coordinates: Array[LongCoordinate]): Array[LongCoordinate] = {
    if (coordinates.isEmpty) {
      return Array.empty
    }
    coordinates.tail.scanLeft(coordinates.head) {
      case (prev, delta) => LongCoordinate(
        prev.x + delta.x,
        prev.y + delta.y
      )
    }
  }

  private def toLongCoordinate(coordinate: Coordinate): LongCoordinate = {
    val x = Math.round(coordinate.x * CoordinatePrecision)
    val y = Math.round(coordinate.y * CoordinatePrecision)
    LongCoordinate(x, y)
  }

  private def toStringCoordinate(coordinate: Coordinate): StringCoordinate = {
    StringCoordinate(coordinate.x.toString, coordinate.y.toString)
  }

  private def toCoordinate(coordinate: LongCoordinate): Coordinate = {
    val x = coordinate.x.toDouble / CoordinatePrecision
    val y = coordinate.y.toDouble / CoordinatePrecision
    new Coordinate(x, y)
  }

  private def stringCoordinateToCoordinate(coordinate: StringCoordinate): Coordinate = {
    new Coordinate(coordinate.x.toDouble, coordinate.y.toDouble)
  }

  private def stringCoordinateToLongCoordinate(coordinate: StringCoordinate): LongCoordinate = {
    LongCoordinate(coordinate.x.toLong, coordinate.y.toLong)
  }

  private def format(coordinates: Array[StringCoordinate]): String = {
    coordinates.map(c => s"[${c.x},${c.y}]").mkString("[", ",", "]")
  }
}
