package kpn.server.analyzer.engine.tiles.domain

import kpn.api.common.data.Way
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.LineString

import scala.math.Pi
import scala.math.atan
import scala.math.exp
import scala.math.log
import scala.math.sin

/*

    EPSG:4326
      WGS84 reference system used by OSM
      See: https://en.wikipedia.org/wiki/Mercator_projection
      coordinates
        longitude (x)
        latitude (y)

    EPSG:3857
      reference system WGS84 Web Mercator used in tiles
      See: https://en.wikipedia.org/wiki/Web_Mercator_projection
      world coordinates
        worldX
          The longitude for a web mercator coordinate where 0 is the international
          date line on the west side, 1 is the international date line on the east
          side, and 0.5 is the prime meridian.
        worldY
          The latitude for a web mercator coordinate where 0 is the north edge of
          the map, 0.5 is the equator, and 1 is the south edge of the map.
 */

object CoordinateTransform {

  private val DEGREES_PER_RADIAN = 180 / Pi
  private val RADIANS_PER_DEGREE = Pi / 180
  private val MAX_LAT = worldYtoLat(-0.1)
  private val MIN_LAT = worldYtoLat(1.1)

  def worldXtoLon(worldX: Double): Double = {
    worldX * 360 - 180
  }

  def worldYtoLat(worldY: Double): Double = {
    val n = Pi - 2 * Pi * worldY
    DEGREES_PER_RADIAN * atan(0.5 * (exp(n) - exp(-n)))
  }

  def lonToWorldX(lon: Double): Double = {
    (lon + 180) / 360
  }

  def latToWorldY(lat: Double): Double = {
    if (lat <= MIN_LAT) return 1.1
    if (lat >= MAX_LAT) return -0.1
    val sinus = sin(lat * RADIANS_PER_DEGREE)
    0.5 - 0.25 * log((1 + sinus) / (1 - sinus)) / Pi
  }

  def toWorldCoordinates(lineString: LineString): Seq[Coordinate] = {
    (0 until lineString.getNumPoints).map { index =>
      val point = lineString.getPointN(index)
      val x = lonToWorldX(point.getX)
      val y = latToWorldY(point.getY)
      new Coordinate(x, y)
    }
  }

  def wayToWorldCoordinates(way: Way): Seq[Coordinate] = {
    way.nodes.map(node => new Coordinate(lonToWorldX(node.lon), latToWorldY(node.lat)))
  }

}
