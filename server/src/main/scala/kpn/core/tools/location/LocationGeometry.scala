package kpn.core.tools.location

import org.locationtech.jts.geom.Envelope
import org.locationtech.jts.geom.Geometry

object LocationGeometry {
  def apply(geometry: Geometry): LocationGeometry = {
    LocationGeometry(
      geometry,
      geometry.getEnvelopeInternal
    )
  }
}

case class LocationGeometry(
  geometry: Geometry,
  envelope: Envelope
) {

  def contains(other: LocationGeometry): Boolean = {
    overlap(other: LocationGeometry) > 0.95
  }

  def overlap(other: LocationGeometry): Double = {
    if (envelope.intersects(other.envelope)) {
      val intersection = geometry.intersection(other.geometry)
      val intersectionArea = intersection.getArea
      val childLocationArea = other.geometry.getArea
      Math.abs(intersectionArea / childLocationArea)
    }
    else {
      0
    }
  }
}
