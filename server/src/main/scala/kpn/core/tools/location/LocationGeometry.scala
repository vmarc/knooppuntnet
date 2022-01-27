package kpn.core.tools.location

import kpn.server.json.Json
import org.apache.commons.io.FileUtils
import org.locationtech.jts.geom.Envelope
import org.locationtech.jts.geom.Geometry

import java.io.File

object LocationGeometry {

  def load(filename: String): LocationGeometry = {
    val string = FileUtils.readFileToString(new File(filename), "UTF-8")
    val geometry = Json.objectMapper.readValue(string, classOf[Geometry])
    LocationGeometry(geometry)
  }

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
