package kpn.server.api.planner.leg

import org.geotools.geometry.jts.JTS
import org.geotools.referencing.CRS
import org.locationtech.jts.geom.Coordinate

object PlanUtil {

  private val sourceCRS = CRS.decode("EPSG:4326") // lat/lon
  private val targetCRS = CRS.decode("EPSG:3857")
  private val transform = CRS.findMathTransform(sourceCRS, targetCRS, false)

  def toCoordinate(lat: Double, lon: Double): Array[Double] = {
    val coordinate = new Coordinate(lat, lon)
    val target = new Coordinate()
    JTS.transform(coordinate, target, transform)
    Array(target.x, target.y)
  }

}
