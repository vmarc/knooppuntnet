package kpn.core.util

import kpn.api.common.LatLon
import kpn.api.common.planner.PlanCoordinate
import org.geotools.geometry.jts.JTS
import org.geotools.referencing.CRS
import org.locationtech.jts.geom.Coordinate

import java.text.DecimalFormat

object CoordinateUtil {

  private val sourceCRS = CRS.decode("EPSG:4326") // lat/lon
  private val targetCRS = CRS.decode("EPSG:3857")
  private val transform = CRS.findMathTransform(sourceCRS, targetCRS, false)
  private val coordinateFormatter = new DecimalFormat("#.########")

  def toCoordinate(lat: Double, lon: Double): PlanCoordinate = {
    val coordinate = new Coordinate(lat, lon)
    val target = JTS.transform(coordinate, null, transform)
    PlanCoordinate(target.x, target.y)
  }

  def toCoordinate2(latitude: String, longitude: String): (String, String) = {
    val coordinate = new Coordinate(latitude.toDouble, longitude.toDouble)
    val target = JTS.transform(coordinate, null, transform)
    (target.x.toString, target.y.toString)
  }

  def toCoordinates(latLons: Seq[LatLon]): String = {
    latLons.map { latLon =>
      val coordinate = new Coordinate(latLon.lat, latLon.lon)
      val transformed = JTS.transform(coordinate, null, transform)
      s"[${formatCoordinate(transformed.x)},${formatCoordinate(transformed.y)}]"
    }.mkString("[", ",", "]")
  }

  def formatCoordinate(value: Double): String = {
    coordinateFormatter.format(value) match {
      case "-0" => "0"
      case other => other
    }
  }
}
