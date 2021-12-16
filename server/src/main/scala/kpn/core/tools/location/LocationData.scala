package kpn.core.tools.location

import org.locationtech.jts.geom.Geometry

case class LocationData(
  id: String,
  doc: LocationDoc,
  geometry: LocationGeometry
) {

  def contains(otherGeometry: Geometry): Boolean = {
    geometry.geometry.contains(otherGeometry)
  }

  def contains(otherLocationGeometry: LocationGeometry): Boolean = {
    geometry.geometry.contains(otherLocationGeometry.geometry)
  }
}
