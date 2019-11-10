package kpn.server.analyzer.engine.analysis.country

import kpn.api.common.Bounds
import org.locationtech.jts.algorithm.locate.IndexedPointInAreaLocator
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.Location

class CountryBoundary(polygons: Seq[Geometry]) {

  private val locators = polygons.map { p => new IndexedPointInAreaLocator(p) }

  def contains(latitude: String, longitude: String): Boolean = {
    val coordinate = new Coordinate(latitude.toDouble, longitude.toDouble)
    locators.exists { locator =>
      locator.locate(coordinate) match {
        case Location.BOUNDARY => true
        case Location.EXTERIOR => false
        case Location.INTERIOR => true
        case Location.NONE => false
        case _ => false
      }
    }
  }

  def bounds: Bounds = {

    val lats = polygons.flatMap(_.getCoordinates.map(_.x))
    val lons = polygons.flatMap(_.getCoordinates.map(_.y))

    Bounds(
      lats.min,
      lons.min,
      lats.max,
      lons.max
    )
  }
}
