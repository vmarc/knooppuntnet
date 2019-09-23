package kpn.core.engine.analysis.location

import org.locationtech.jts.algorithm.locate.IndexedPointInAreaLocator
import org.locationtech.jts.geom.Coordinate

object LocationLocator {
  def from(definition: LocationDefinition): LocationLocator = {
    val children = definition.children.map(LocationLocator.from)
    new LocationLocator(definition, children)
  }
}

class LocationLocator(val locationDefinition: LocationDefinition, val children: Seq[LocationLocator]) {

  private val locator = new IndexedPointInAreaLocator(locationDefinition.geometry)

  def contains(latitude: String, longitude: String): Boolean = {
    val coordinate = new Coordinate(longitude.toDouble, latitude.toDouble)
    locationDefinition.boundingBox.contains(coordinate) && geometryContains(coordinate)
  }

  private def geometryContains(coordinate: Coordinate): Boolean = {
    locator.locate(coordinate) match {
      case org.locationtech.jts.geom.Location.BOUNDARY => true
      case org.locationtech.jts.geom.Location.EXTERIOR => false
      case org.locationtech.jts.geom.Location.INTERIOR => true
      case org.locationtech.jts.geom.Location.NONE => false
      case _ => false
    }
  }

}
