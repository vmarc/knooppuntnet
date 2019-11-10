package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.Language
import org.locationtech.jts.geom.Envelope
import org.locationtech.jts.geom.Geometry

case class LocationDefinition(
  name: String,
  locationNames: Map[Language, String],
  filename: String,
  boundingBox: Envelope,
  geometry: Geometry,
  children: Seq[LocationDefinition] = Seq.empty
) {

  def name(language: Language): String = {
    locationNames.getOrElse(language, name)
  }

  def names: Seq[String] = {
    locationNames.keys.map(key => key.toString + "=" + locationNames(key)).toSeq
  }

}
