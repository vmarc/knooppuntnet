package kpn.server.opendata.france

import kpn.api.common.LatLonImpl
import mil.nga.geopackage.features.user.FeatureRow
import org.geotools.geometry.jts.JTS
import org.geotools.referencing.CRS
import org.locationtech.jts.geom.{Coordinate, GeometryFactory, Point, PrecisionModel}

object FranceUtil {

  private val sourceCRS = CRS.decode("EPSG:2154") // RGF93 v1 / Lambert-93 - France
  private val targetCRS = CRS.decode("EPSG:4326")
  private val transform = CRS.findMathTransform(sourceCRS, targetCRS, false)
  private val geometryFactory = new GeometryFactory(new PrecisionModel, 31370)

  def lambertToLatLon(x: Double, y: Double): LatLonImpl = {
    val sourcePoint = geometryFactory.createPoint(new Coordinate(x, y))
    val targetPoint = JTS.transform(sourcePoint, transform).asInstanceOf[Point]
    val latitude = targetPoint.getX.toString
    val longitude = targetPoint.getY.toString
    LatLonImpl(latitude, longitude)
  }

  def routeNames(row: FeatureRow): Seq[String] = {
    row.getValue("iti_nom").toString.split(";").toSeq.flatMap(_.split(",")).map(_.trim).filterNot(_.isEmpty)
  }

}
