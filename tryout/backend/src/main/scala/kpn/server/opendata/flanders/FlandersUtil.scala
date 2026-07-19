package kpn.server.opendata.flanders

import kpn.api.common.LatLonImpl
import org.geotools.geometry.jts.JTS
import org.geotools.referencing.CRS
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.PrecisionModel

object FlandersUtil {

  private val sourceCRS = CRS.decode("EPSG:31370")
  private val targetCRS = CRS.decode("EPSG:4326")
  private val transform = CRS.findMathTransform(sourceCRS, targetCRS, false)
  private val geometryFactory = new GeometryFactory(new PrecisionModel, 31370)

  def lambertToLatLon(x: String, y: String): LatLonImpl = {
    val sourcePoint = geometryFactory.createPoint(new Coordinate(x.toDouble, y.toDouble))
    val targetPoint = JTS.transform(sourcePoint, transform).asInstanceOf[Point]
    val latitude = targetPoint.getX.toString
    val longitude = targetPoint.getY.toString
    LatLonImpl(latitude, longitude)
  }
}
