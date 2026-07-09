package kpn.server.analyzer.engine.analysis.location

import kpn.core.doc.LocationPath
import kpn.core.tools.location.LocationGeometry
import org.locationtech.jts.algorithm.locate.IndexedPointInAreaLocator
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Location

case class LocationStoreData(
  id: String,
  paths: Seq[LocationPath],
  name: String,
  geometry: LocationGeometry,
  locators: Seq[IndexedPointInAreaLocator],
  children: Seq[LocationStoreData] = Seq.empty
) {

  def contains(latitude: String, longitude: String): Boolean = {
    val coordinate = new Coordinate(longitude.toDouble, latitude.toDouble)
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
}
