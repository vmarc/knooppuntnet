package kpn.core.util

import org.geotools.geometry.jts.GeometryClipper
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Envelope
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LinearRing
import org.locationtech.jts.geom.impl.CoordinateArraySequence

object GeometryUtil {

  private val factory = new GeometryFactory()

  def clip(geometry: Geometry, envelope: Envelope): Geometry = {
    val clipper = new GeometryClipper(envelope)
    clipper.clipSafe(geometry, false, 0.00001)
  }

  // tip: take coordinates from https://tools.geofabrik.de/calc
  def envelope(left: Double, bottom: Double, right: Double, top: Double): Envelope = {
    val coordinates = Seq(
      new Coordinate(left, top),
      new Coordinate(right, top),
      new Coordinate(right, bottom),
      new Coordinate(left, bottom),
      new Coordinate(left, top)
    )
    val boundingBox = new LinearRing(new CoordinateArraySequence(coordinates.toArray), factory)
    boundingBox.getEnvelopeInternal
  }
}
