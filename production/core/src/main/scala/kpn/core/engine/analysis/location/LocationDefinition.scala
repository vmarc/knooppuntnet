package kpn.core.engine.analysis.location

import kpn.shared.Language
import org.locationtech.jts.geom.Envelope
import org.locationtech.jts.geom.Geometry

case class LocationDefinition(
  level: Int,
  name: String,
  locationNames: Map[Language, String],
  boundingBox: Envelope,
  area: Double,
  geometry: Geometry,
  children: Seq[LocationDefinition] = Seq.empty
) {

  def name(language: Language): String = {
    locationNames.getOrElse(language, name)
  }

  def names: Seq[String] = {
    locationNames.keys.map(key => key.toString + "=" + locationNames(key)).toSeq
  }

  def contains(other: LocationDefinition): Boolean = {
    val intersection = geometry.intersection(other.geometry)
    val a1 = intersection.getArea
    val a2 = other.area
    Math.abs(a1 / a2) > 0.95
    // geometry.contains(other.geometry)
  }

}
