package kpn.server.api.planner.leg

import kpn.api.common.planner.PlanCoordinate
import org.geotools.geometry.jts.JTS
import org.geotools.referencing.CRS
import org.locationtech.jts.geom.Coordinate

object PlanUtil {

  private val sourceCRS = CRS.decode("EPSG:4326") // lat/lon
  private val targetCRS = CRS.decode("EPSG:3857")
  private val transform = CRS.findMathTransform(sourceCRS, targetCRS, false)

  def toCoordinate(lat: Double, lon: Double): PlanCoordinate = {
    val coordinate = new Coordinate(lat, lon)
    val target = JTS.transform(coordinate, new Coordinate(), transform)
    PlanCoordinate(target.x, target.y)
  }

}
