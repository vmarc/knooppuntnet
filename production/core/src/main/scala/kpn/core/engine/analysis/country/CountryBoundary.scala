package kpn.core.engine.analysis.country

import com.vividsolutions.jts.algorithm.locate.IndexedPointInAreaLocator
import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.Geometry
import com.vividsolutions.jts.geom.Location
import kpn.shared.Bounds

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
